package cn.bowseros.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.bowseros.config.ListItem;
import cn.bowseros.entity.NodebookContent;
import cn.bowseros.entity.NodebookItem;
import cn.bowseros.entity.People;
import cn.bowseros.vo.NodebookVo;
import cn.bowseros.vo.NodesCount;
import codebook.commons.R;
import codebook.commons.ValueUtils;

/**
 *
 * Created by bowser Sep 7, 2019/2:59:56 PM
 * 
 */
@Transactional
@Service
public class NodebookItemServiceImpl extends BaseService implements NodebookItemService {

	/**
	 * 新增笔记分组
	 */
	// private String nodeGroupKey = "nodeGroupKey_";//新增笔记分组对操作符,用于限制用户暴力灌水
	// private String xhx = "_";
	@Override
	public R<String> insertGroup(NodebookItem nodebookItem) {
		if (ValueUtils.isEmpty(nodebookItem.getNodeBookName())) {
			return new R<>(500, "没有输入分组名称");
		}

		People p = (People) session.getAttribute("user_");
		if (p == null) {
			return new R<>(766, "未登录");
		}

		nodebookItem.setId(String.valueOf(gid.nextId()));
		nodebookItem.setpId("0");
		nodebookItem.setUpdateTime(new Date());
		nodebookItem.setUserid(p.getId());
		int r = nodebookItemMapper.insert(nodebookItem);

		if (r > 0) {
			String cacheId = "mynodes" + p.getId();
			dbStore.remove(cacheId);
		}
		return r > 0 ? R.SUCCESS : R.FAIL;
	}

	/**
	 * 查询我的笔记节点
	 */
	@Override
	public R<List<NodebookItem>> mynodes() {
		People p = (People) session.getAttribute("user_");
		if (p == null) {
			return new R<>(766, "未登录");
		}

		String cacheId = "mynodes" + p.getId();
		String mynodesJson = dbStore.get(cacheId);
		List<NodebookItem> nodes = null;
		if (mynodesJson != null) {
			nodes = JSON.parseArray(mynodesJson, NodebookItem.class);
		} else {
			QueryWrapper<NodebookItem> queryWrapper = new QueryWrapper<NodebookItem>();
			queryWrapper.eq("userid", p.getId());
			queryWrapper.and(i -> i.eq("share", 0));
			queryWrapper.orderByDesc("id");
			nodes = nodebookItemMapper.selectList(queryWrapper);
			dbStore.put(cacheId, JSON.toJSONString(nodes));
		}

		p = peopleMapper.selectById(p.getId());
		Map<String, Object> systemSetting = new HashMap<String, Object>();
		systemSetting.put("allowdelete", p.getAllowdelete());// 删除开关
		return new R<List<NodebookItem>>(nodes, systemSetting);
	}

	/**
	 * 保存新笔记
	 */
	@Override
	public R<String> insert(NodebookVo nodebookContent) {
		People p = (People) session.getAttribute("user_");
		if (p == null) {
			return new R<>(766, "未登录");
		}
		
		//笔记标签
		String tag = nodebookContent.getTag();

		if (!ValueUtils.isEmpty(nodebookContent.getId())) {
			NodebookContent nc = nodebookContentMapper.selectById(nodebookContent.getId());
			nc.setNodecontent(nodebookContent.getNodecontent());

			NodebookItem ni = nodebookItemMapper.selectById(nodebookContent.getId());
			if (!ni.getUserid().equals(p.getId())) {
				// 如果不是本人的笔记则不允许修改
				return new R<>(776, "你不可以修改这篇笔记!");
			}
			// share > 0 表示笔记涉嫌违规
			if (ni.getShare() > 0) {
				return new R<>(776, ni.getSharemsg());
			}
			
			// 笔记标签
			if (!ValueUtils.isEmpty(tag)) {
				Integer nodetypeNo = (Integer) ValueUtils.isEmptyUseDefault(nodetypeMap.get(tag), 999);
				if (nodetypeNo != 999) {
					ni.setNodetype(nodetypeNo);
				}
			}
			
			ni.setpId(nodebookContent.getpId());
			ni.setNodeBookName(nodebookContent.getNodeBookName());
			ni.setUpdateTime(new Date());

			nodebookContentMapper.updateById(nc);
			nodebookItemMapper.updateById(ni);

			dbStore.remove(nodebookContent.getId());
			dbStore.remove("mynodes" + p.getId());
			return R.SUCCESS;
		}

		String userId = p.getId();
		String id = String.valueOf(gid.nextId());
		NodebookItem ni = new NodebookItem();
		ni.setId(id);
		ni.setNodeBookName(nodebookContent.getNodeBookName());
		ni.setOpen("false");
		ni.setParent("false");
		ni.setpId(nodebookContent.getpId());
		ni.setShare(0);
		ni.setUpdateTime(new Date());
		ni.setUserid(userId);
		// 笔记标签
		if (!ValueUtils.isEmpty(tag)) {
			Integer nodetypeNo = (Integer) ValueUtils.isEmptyUseDefault(nodetypeMap.get(tag), 999);
			if (nodetypeNo != 999) {
				ni.setNodetype(nodetypeNo);
			}
		}
		
		NodebookContent nc = new NodebookContent();
		nc.setNid(id);
		nc.setNodecontent(nodebookContent.getNodecontent());

		nodebookItemMapper.insert(ni);
		nodebookContentMapper.insert(nc);

		dbStore.remove("mynodes" + p.getId());
		return R.SUCCESS;
	}

	/**
	 * 查询我的笔记分组
	 */
	@Override
	public R<List<Map<String, Object>>> mynodesGroup() {
		People p = (People) session.getAttribute("user_");
		QueryWrapper<NodebookItem> queryWrapper = new QueryWrapper<NodebookItem>();
		queryWrapper.select("id,nodeBookName");
		queryWrapper.eq("userid", p.getId());
		queryWrapper.and(i -> i.eq("share", 0));// 查询没有违规的笔记
		queryWrapper.and(i -> i.eq("pId", "0"));
		List<Map<String, Object>> myGroups = nodebookItemMapper.selectMaps(queryWrapper);
		return new R<List<Map<String, Object>>>(myGroups);
	}

	/**
	 * 查询我的笔记内容
	 */
	@Override
	public R<NodebookVo> mynodesContent(String nodebookid) {
		NodebookVo nv = new NodebookVo();
		People p = (People) session.getAttribute("user_");

		// 已登陆的用户可以走缓存
		if (p != null) {
			String nodebookVoJson = dbStore.get(nodebookid);
			if (nodebookVoJson != null) {
				nv = JSON.parseObject(nodebookVoJson, NodebookVo.class);
				// share > 0 表示这篇笔记违规了,被管理员屏蔽了
				if (nv.getShare() > 0) {
					return null;
				}
				return new R<NodebookVo>(nv);
			}
		}

		NodebookItem ni = nodebookItemMapper.selectById(nodebookid);
		if (ni == null) {
			return null;
		}
		// share > 0 表示这篇笔记违规了,被管理员屏蔽了
		if (ni.getShare() > 0) {
			ni = null;
			return null;
		}
		
		NodebookContent nc = nodebookContentMapper.selectById(nodebookid);
		if (nc == null) {
			return null;
		}

		nv.setNodeBookName(ni.getNodeBookName());
		nv.setpId(ni.getpId());
		nv.setNodecontent(nc.getNodecontent());
		nv.setUpdateTime(ni.getUpdateTime());

		dbStore.put(nodebookid, JSON.toJSONString(nv));
		return new R<NodebookVo>(nv);
	}

	/**
	 * 查询相关笔记
	 */
	@Override
	public R<List<NodebookVo>> matter(String selectedText) {
//		People p = (People) session.getAttribute("user_");
//		QueryWrapper<NodebookItem> queryWrapper = new QueryWrapper<NodebookItem>();
//		queryWrapper.eq("userid",p.getId());
//		queryWrapper.orderByDesc("id");
//		List<NodebookItem> nodes = nodebookItemMapper.selectList(queryWrapper);

		R<List<NodebookItem>> r = mynodes();
		if (r.getCode() != 200) {
			return new R<>(r.getCode(), r.getMsg());
		}

		List<NodebookItem> nodes = r.getContent();
		List<NodebookVo> nvList = new ArrayList<NodebookVo>();
		NodebookVo nv = null;
		for (NodebookItem nodebookItem : nodes) {
			if (nodebookItem.getpId().equals("0")) {
				continue;
			}
			if (matter(nodebookItem.getNodeBookName(), selectedText)) {
				nv = new NodebookVo();
				nv.setId(nodebookItem.getId());
				nv.setNodeBookName(nodebookItem.getNodeBookName());
				nv.setUpdateTime(nodebookItem.getUpdateTime());
				nvList.add(nv);
			}
		}

		nodes = null;
		return new R<List<NodebookVo>>(nvList);
	}

	public static boolean matter(String source, String target) {
		Pattern pattern = Pattern.compile(target, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(source);
		return matcher.find();
//		source = source.toLowerCase();
//		target = target.toLowerCase();
//		
//		int ppx = 0;//匹配性
//		char [] c = source.toCharArray();
//		int lxx = 0;//连续性
//		for (char d : c) {
//			if(target.contains(String.valueOf(d))) {
//				ppx ++;
//				lxx ++;
//				continue;
//			}
//			if (lxx > 0) {
//				lxx --;
//			}
//		}
//		return lxx >= 2;
	}

	/**
	 * 删除笔记
	 */
	@Override
	public R<String> delete(NodebookVo nodebookVo) {
		People p = (People) session.getAttribute("user_");
		if (p == null) {
			return new R<>(766, "未登录");
		}

		NodebookItem ni = nodebookItemMapper.selectById(nodebookVo.getId());
		if (ni == null) {
			return new R<String>(767, "笔记不存在");
		}
		//笔记属于者与操作者不一致
		if (!ni.getUserid().equals(p.getId())) {
			return new R<String>(777, "笔记不存在");
		}
		//笔记已被管理员设置为不可操作
		if (ni.getShare() > 0) {
			return new R<String>(777, ni.getSharemsg());
		}

		p = peopleMapper.selectById(p.getId());
		if (p.getAllowdelete() != 1) {//用户没有打开删除开关
			return new R<String>(767, "不允许删除操作");
		}

		ni.setShare(1);
		ni.setSharemsg("已删除");
		nodebookItemMapper.updateById(ni);
		//nodebookContentMapper.deleteById(ni.getId());
		//nodebookItemMapper.deleteById(ni.getId());
		dbStore.remove("mynodes" + p.getId());
		return R.SUCCESS;
	}

	/**
	 * 获取共享笔记
	 */
	@Override
	public Map<String, Object> getOpenshare(long current, long size) {
		// 查询共享用户
		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
		queryWrapper.eq("openshare", "1");
		List<People> plist = peopleMapper.selectList(queryWrapper);
		Set<String> pids = new HashSet<>();
		for (People people : plist) {
			pids.add(people.getId());
		}
		if (pids.size() == 0) {
			return new HashMap<>();
		}

		// 查询共享用户的笔记列表
		QueryWrapper<NodebookItem> nodequeryWrapper = new QueryWrapper<NodebookItem>();
		nodequeryWrapper.in("userid", pids);
		nodequeryWrapper.and(i -> i.gt("pId", 0));
		nodequeryWrapper.and(i -> i.eq("share", 0));// 这个是给管理者用的设置为大于0的数可屏蔽笔记
		nodequeryWrapper.orderByDesc("updateTime");

		current = (current <= 0) ? 1 : current;
		Page<NodebookItem> page = new Page<>(current, size);
		IPage<NodebookItem> nList = nodebookItemMapper.selectPage(page, nodequeryWrapper);
		long pages = nList.getPages();
		List<NodebookItem> result = nList.getRecords();
		Map<String, Object> r = new HashMap<>();
		r.put("pages", pages);
		r.put("result", result);

		return r;
	}

	/**
	 * 笔记数量排名
	 */
	@Override
	public List<NodesCount> nodebookCount() {
//		QueryWrapper<NodebookItem> nodequeryWrapper = new QueryWrapper<NodebookItem>();
//		nodequeryWrapper.select("userid,COUNT(*) as num");
//		nodequeryWrapper.gt("pId", 0);
//		nodequeryWrapper.groupBy("userid");
//		nodequeryWrapper.orderByDesc("num");
//		nodequeryWrapper.last("limit 10");
//		
//		List<Map<String, Object>> result = nodebookItemMapper.selectMaps(nodequeryWrapper);
//		Set<String> uids = new HashSet<>();
//		Map<String, Object> pCount = new HashMap<>(16);
//		for (Map<String, Object> m : result) {
//			uids.add(m.get("userid").toString());
//			pCount.put(m.get("userid").toString(), m.get("num").toString());
//		}
//		
//		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
//		queryWrapper.in("id", uids);
//		List<People> peps = peopleMapper.selectList(queryWrapper);
//		List<NodesCount> nodesCountList = new ArrayList<>();
//		NodesCount nc = null;
//		for (People people : peps) {
//			nc = new NodesCount();
//			nc.setName(people.getUname());
//			nc.setNum(Integer.valueOf(pCount.get(people.getId()).toString()));
//			nodesCountList.add(nc);
//		}
//		result = null;
//		peps = null;
//		
//		Collections.sort(nodesCountList);
//		
//		// 1 /static/img/jp.png
//		// data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAB4ElEQVQ4jX1QTWvUUBQ9N+8lZD6KiFBxiu4asUyni24cpLXSWXQhuPI/+ANaxI2TlVCm4E76E9yruxTHKlTcTqWYcW1wQKRtMsQm710XbWrmI97dPfeec889hFx9Xl//wMAqAIDoQdPzugBw0Gqtgfk9ABCwf3dv737GMfICTNQ2bTu6ubQEaZo7GS6k7NxqNNgqlUImauc5Mt80Pa/7ZWPjiJiXDSFuH7RaawBgCHGHiUgr9S1zNdUBAKg03Qp8f3jdcarSNHeElJ0bjlP+6fthmiSb4/sTAk3P62qljoiZQbRAQtSLrk8VyLuQllWqOY5ddH0ig6waz5ev/Pl1Jq2rJkCEyu95aV6zZuB5E7s0Dgw/PXsJoifWXM02qjMAAB2eIgmCiDXvlu+92CwUiPafPiIhXtvOvE1i1ByrFLHfj1knjysrnbfTMxBG25qrXZKHhz0MD3vnl4TExcwtzkCrhcw2AJizsyNjo1oFUl0fwcYz+G/xJDQiQIb4qsPTyz4ZDJAMBv8MRiEgjF6hAGvtJkEQsUoBAOX6Isr1xfOZSnEW/IhYabdQoLKy/YY178Z+P1Ynx2CtwFpBnRwj/t6PoPSryur2uxHX016NPm49JCFdVqp+8VqPlXbHyQDwF+sF0G8ENFzgAAAAAElFTkSuQmCC
//
//		// 2 /static/img/yp.png
//		// data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAACBklEQVQ4jX2Ry2sTURTGvzP3TpyYJiWFIBG0hepIQ2ilCcW4iJUGm4Xgyk3RveA2dpvsXDTgTtwKov0DFHxMMWZhpcYH1BCcWmi7UBrQltoJhcy910VNmbx6Vveex++c7xyCxz7MzLxTQBoAQHQ1ZVklAFjOZKah1FsAIKB8aWnpSqtG8wIUUV43DOfMxAS4rhdbfsb5wtnxceXz+/cVUd5bw72flGWVVrLZGimV0Bi7sJzJTAOAxtiYIiIpxPfWVD0nAADhuvd+2XbjlGkOcF0vMs4XoqZ5ctu2991mM9eZ3wVIWVZJClEjpRSIYsRYvF/3ngDvFNzn8582TaNf964dtMzO3R8c2PnND0Jh1IhwYuoPdwYjQViJrlzqdCy++fJAge6YIxEjHAwAAHb3GljbqjtCykdz1yZzfQGPX3684WP64lR82NCYBo0Ow1IpSCGx8m3zQEhx89Zs4nlPCT7G8+ZIxHj/db2XMoyNRg17Y7sA4AjQtkQpZCwcDCCdNAEA6aTZ9h4KBaAk4t6anlc43lTbr02Cxqi6u9eYrK7/BACUK/ZRrFyxETsXhcZotS+g2VSFta36s8sXRwOcs7ZOritQqW44rpAFr7/rjE9ffy4SaXfPD0eModDhGXf+OvixWXeEkA/nZhPzxwIA4MmrT9c1ooKSKv5f2qorZOF2NvmiM/cf7sTJP/6yuMUAAAAASUVORK5CYII=
//		
//		// 3 
//		// data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAACDUlEQVQ4jXWSz2sTQRTHv5OZye5WSyGBiIIectjU2qRSKTQHY+vmUKEYEARRvPsH1JNg1ouXFrwV/4FqToJS8bKxMZeYniRRYvZQEBV/9MepqTazk+mhpGySzbvNd977vPe+MwS++GhZHxSQAQAQMp92nBIAVLLZOSi1AQAEKM8Wi9e6NSE/QBGS57reOj81Bcb5SlenjC1fSKVU2DD2FSF5fw3zH9KOU9pcWGgQpa6EKE1Ustk5AAhRelERQjpSNrtTBU4AANLzHv5y3YMzpnmacb5CGVs+a5ojf1x33xNiqT9/AJB2nFJHygZRSoGQCULp5LDugQD/FCwcNs6Zpj6s+4AH3di+GhvTDhU71Ai+4y/0y2PsgI+OwhnMJf3C+tP7zyhCD1LjCT0WjR4D93ZRb7otIcXzxUdrS0MBb57czXHOC9fTs3qY8x5wWwi8r1T+/2vL27fstfXAFRin+dR4Qn9XLgdthplkUv/0tWEDOAH0mOhJORGLRpGzLABAIh5HIh4HAOQsC7FIBNKTk/6awFcYFipA612B8i/be7vTm7U6AKC5tXVy97pYxEwyCcpofSigLdp2vem+vJHJnAoycaNabXlC2H6d+g+F0mf3zvylyLcfP6dHDIMZmoZOp4PfOzuo1motIbzVm49frPprBv4BALyy7y1q4ZAt5bFhlNK6J4Sdyxfe9uceAQ5kzAxUmf+UAAAAAElFTkSuQmCC
//		
//		nodesCountList.get(0).setTopStyle("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAB4ElEQVQ4jX1QTWvUUBQ9N+8lZD6KiFBxiu4asUyni24cpLXSWXQhuPI/+ANaxI2TlVCm4E76E9yruxTHKlTcTqWYcW1wQKRtMsQm710XbWrmI97dPfeec889hFx9Xl//wMAqAIDoQdPzugBw0Gqtgfk9ABCwf3dv737GMfICTNQ2bTu6ubQEaZo7GS6k7NxqNNgqlUImauc5Mt80Pa/7ZWPjiJiXDSFuH7RaawBgCHGHiUgr9S1zNdUBAKg03Qp8f3jdcarSNHeElJ0bjlP+6fthmiSb4/sTAk3P62qljoiZQbRAQtSLrk8VyLuQllWqOY5ddH0ig6waz5ev/Pl1Jq2rJkCEyu95aV6zZuB5E7s0Dgw/PXsJoifWXM02qjMAAB2eIgmCiDXvlu+92CwUiPafPiIhXtvOvE1i1ByrFLHfj1knjysrnbfTMxBG25qrXZKHhz0MD3vnl4TExcwtzkCrhcw2AJizsyNjo1oFUl0fwcYz+G/xJDQiQIb4qsPTyz4ZDJAMBv8MRiEgjF6hAGvtJkEQsUoBAOX6Isr1xfOZSnEW/IhYabdQoLKy/YY178Z+P1Ynx2CtwFpBnRwj/t6PoPSryur2uxHX016NPm49JCFdVqp+8VqPlXbHyQDwF+sF0G8ENFzgAAAAAElFTkSuQmCC");
//		nodesCountList.get(1).setTopStyle("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAACBklEQVQ4jX2Ry2sTURTGvzP3TpyYJiWFIBG0hepIQ2ilCcW4iJUGm4Xgyk3RveA2dpvsXDTgTtwKov0DFHxMMWZhpcYH1BCcWmi7UBrQltoJhcy910VNmbx6Vveex++c7xyCxz7MzLxTQBoAQHQ1ZVklAFjOZKah1FsAIKB8aWnpSqtG8wIUUV43DOfMxAS4rhdbfsb5wtnxceXz+/cVUd5bw72flGWVVrLZGimV0Bi7sJzJTAOAxtiYIiIpxPfWVD0nAADhuvd+2XbjlGkOcF0vMs4XoqZ5ctu2991mM9eZ3wVIWVZJClEjpRSIYsRYvF/3ngDvFNzn8582TaNf964dtMzO3R8c2PnND0Jh1IhwYuoPdwYjQViJrlzqdCy++fJAge6YIxEjHAwAAHb3GljbqjtCykdz1yZzfQGPX3684WP64lR82NCYBo0Ow1IpSCGx8m3zQEhx89Zs4nlPCT7G8+ZIxHj/db2XMoyNRg17Y7sA4AjQtkQpZCwcDCCdNAEA6aTZ9h4KBaAk4t6anlc43lTbr02Cxqi6u9eYrK7/BACUK/ZRrFyxETsXhcZotS+g2VSFta36s8sXRwOcs7ZOritQqW44rpAFr7/rjE9ffy4SaXfPD0eModDhGXf+OvixWXeEkA/nZhPzxwIA4MmrT9c1ooKSKv5f2qorZOF2NvmiM/cf7sTJP/6yuMUAAAAASUVORK5CYII=");
//		nodesCountList.get(2).setTopStyle("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAACDUlEQVQ4jXWSz2sTQRTHv5OZye5WSyGBiIIectjU2qRSKTQHY+vmUKEYEARRvPsH1JNg1ouXFrwV/4FqToJS8bKxMZeYniRRYvZQEBV/9MepqTazk+mhpGySzbvNd977vPe+MwS++GhZHxSQAQAQMp92nBIAVLLZOSi1AQAEKM8Wi9e6NSE/QBGS57reOj81Bcb5SlenjC1fSKVU2DD2FSF5fw3zH9KOU9pcWGgQpa6EKE1Ustk5AAhRelERQjpSNrtTBU4AANLzHv5y3YMzpnmacb5CGVs+a5ojf1x33xNiqT9/AJB2nFJHygZRSoGQCULp5LDugQD/FCwcNs6Zpj6s+4AH3di+GhvTDhU71Ai+4y/0y2PsgI+OwhnMJf3C+tP7zyhCD1LjCT0WjR4D93ZRb7otIcXzxUdrS0MBb57czXHOC9fTs3qY8x5wWwi8r1T+/2vL27fstfXAFRin+dR4Qn9XLgdthplkUv/0tWEDOAH0mOhJORGLRpGzLABAIh5HIh4HAOQsC7FIBNKTk/6awFcYFipA612B8i/be7vTm7U6AKC5tXVy97pYxEwyCcpofSigLdp2vem+vJHJnAoycaNabXlC2H6d+g+F0mf3zvylyLcfP6dHDIMZmoZOp4PfOzuo1motIbzVm49frPprBv4BALyy7y1q4ZAt5bFhlNK6J4Sdyxfe9uceAQ5kzAxUmf+UAAAAAElFTkSuQmCC");
//		
		return ListItem.nodesCountList;
	}

	/**
	 * 查询我的节点数量
	 */
	@Override
	public Map<String, String> mynodesNum() {
		
		Map<String, String> result = new HashMap<>();
		People p = (People) session.getAttribute("user_");
		if (p == null) {
			result.put("groupCount", "0");
			result.put("nodeCount", "0");
			return result;
		}
		
		QueryWrapper<NodebookItem> queryWrapper = new QueryWrapper<NodebookItem>();
		queryWrapper.eq("userid", p.getId());
		queryWrapper.and(i -> i.eq("parent", "true"));
		queryWrapper.and(i -> i.eq("share", 0));
		Integer groupCount = nodebookItemMapper.selectCount(queryWrapper);

		QueryWrapper<NodebookItem> nodeCountqueryWrapper = new QueryWrapper<NodebookItem>();
		nodeCountqueryWrapper.eq("userid", p.getId());
		nodeCountqueryWrapper.and(i -> i.eq("parent", "false"));
		nodeCountqueryWrapper.and(i -> i.eq("share", 0));
		Integer nodeCount = nodebookItemMapper.selectCount(nodeCountqueryWrapper);

		p = peopleMapper.selectById(p.getId());
		String background = ValueUtils.isEmptyUseDefault(p.getUbackground(), "cloth.png");
		result.put("ubackground", background);
		result.put("groupCount", groupCount.toString());
		result.put("nodeCount", nodeCount.toString());
		return result;
	}

	/**
	 * 开发者笔记总条数
	 */
	@Override
	public Integer nodeCount() {
		return nodebookContentMapper.selectCount(null);
	}
	
	public static Map<String,Integer> nodetypeMap = new HashMap<>();
	static {
		nodetypeMap.put("jq", 1);
		nodetypeMap.put("js", 2);
		nodetypeMap.put("vue", 3);
		nodetypeMap.put("bug", 4);
		nodetypeMap.put("java", 5);
		nodetypeMap.put("kafka", 6);
		nodetypeMap.put("mysql", 7);
		nodetypeMap.put("redis", 8);
		nodetypeMap.put("gp", 9);
		nodetypeMap.put("algorithm", 10);//算法
		nodetypeMap.put("c", 11);//c语言
		nodetypeMap.put("python", 12);//python
		nodetypeMap.put("xml", 13);//xml
		nodetypeMap.put("json", 14);//json
		nodetypeMap.put("linux", 15);//linux
		nodetypeMap.put("php", 16);//php
		nodetypeMap.put("springcloud", 17);//springcloud
		nodetypeMap.put("springboot", 18);//php
		nodetypeMap.put("container", 19);//container 容器
	}
	
	/**
	 * 不同的笔记节点类型查询
	 */
	@Override
	public Map<String, Object> getOpenshareByType(String nodetype, int currentpage, int size) {
		nodetype = (ValueUtils.isEmptyUseDefault(nodetype, "blank"));
		//节点类型编号
		Integer nodetypeNo = (Integer) ValueUtils.isEmptyUseDefault(nodetypeMap.get(nodetype), 999);
		Map<String, Object> r = new HashMap<>();
		if (nodetypeNo == 999) {
			r.put("pages", 0);
			r.put("result", new ArrayList<>());
			return r;
		}
		
		// 查询共享用户
		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
		queryWrapper.eq("openshare", "1");
		List<People> plist = peopleMapper.selectList(queryWrapper);
		Set<String> pids = new HashSet<>();
		for (People people : plist) {
			pids.add(people.getId());
		}
		if (pids.size() == 0) {
			return new HashMap<>();
		}

		// 查询共享用户的笔记列表
		QueryWrapper<NodebookItem> nodequeryWrapper = new QueryWrapper<NodebookItem>();
		nodequeryWrapper.in("userid", pids);
		nodequeryWrapper.and(i -> i.gt("pId", 0));
		nodequeryWrapper.and(i -> i.eq("share", 0));// 这个是给管理者用的设置为大于0的数可屏蔽笔记
		nodequeryWrapper.and(i -> i.eq("nodetype", nodetypeNo));//节点类型编号
		nodequeryWrapper.orderByDesc("updateTime");

		currentpage = (currentpage <= 0) ? 1 : currentpage;
		Page<NodebookItem> page = new Page<>(currentpage, size);
		IPage<NodebookItem> nList = nodebookItemMapper.selectPage(page, nodequeryWrapper);
		long pages = nList.getPages();
		List<NodebookItem> result = nList.getRecords();
		
		r.put("pages", pages);
		r.put("result", result);
		return r;
	}

}
