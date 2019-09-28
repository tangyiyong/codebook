package cn.bowseros.scheduled;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.bowseros.config.ListItem;
import cn.bowseros.entity.NodebookItem;
import cn.bowseros.entity.People;
import cn.bowseros.service.BaseService;
import cn.bowseros.vo.NodesCount;
import codebook.commons.SimpleBloomFilter;

/**
 *
 * Created by bowser Aug 26, 2019/8:13:13 PM
 * 
 */
@Component
public class ScheduledTasks extends BaseService{

	private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	/**
	 * 每小时注册用户的IP
	 */
	@Scheduled(fixedRate = 3600000)
	public void toDayRegisterList() {
		logger.info("刷新每小时用户注册列表开始");
		ListItem.toDayRegisterList = null;
		ListItem.toDayRegisterList = new SimpleBloomFilter();
		logger.info("刷新每小时用户注册列表结束");
	}
	
	/**
	 * 每小时用户笔记数量排名
	 * 每日 0 0 0 1/1 * ? 
	 */
	@Scheduled(fixedRate = 3600000)
	public void userNodesTop() {
		logger.info("刷新每小时用户笔记数量排名开始");
		QueryWrapper<NodebookItem> nodequeryWrapper = new QueryWrapper<NodebookItem>();
		nodequeryWrapper.select("userid,COUNT(*) as num");
		nodequeryWrapper.gt("pId", 0);
		nodequeryWrapper.and(i->i.eq("share", 0));
		nodequeryWrapper.groupBy("userid");
		nodequeryWrapper.orderByDesc("num");
		nodequeryWrapper.last("limit 10");
		
		List<Map<String, Object>> result = nodebookItemMapper.selectMaps(nodequeryWrapper);
		Set<String> uids = new HashSet<>();
		Map<String, Object> pCount = new HashMap<>(16);
		for (Map<String, Object> m : result) {
			uids.add(m.get("userid").toString());
			pCount.put(m.get("userid").toString(), m.get("num").toString());
		}
		
		QueryWrapper<People> queryWrapper = new QueryWrapper<People>();
		queryWrapper.in("id", uids);
		List<People> peps = peopleMapper.selectList(queryWrapper);
		NodesCount nc = null;
		ListItem.nodesCountList.clear();//先清理
		for (People people : peps) {
			nc = new NodesCount();
			nc.setName(people.getUname());
			nc.setNum(Integer.valueOf(pCount.get(people.getId()).toString()));
			ListItem.nodesCountList.add(nc);
		}
		result = null;
		peps = null;
		
		Collections.sort(ListItem.nodesCountList);
		
		// 1 /static/img/jp.png
		// data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAB4ElEQVQ4jX1QTWvUUBQ9N+8lZD6KiFBxiu4asUyni24cpLXSWXQhuPI/+ANaxI2TlVCm4E76E9yruxTHKlTcTqWYcW1wQKRtMsQm710XbWrmI97dPfeec889hFx9Xl//wMAqAIDoQdPzugBw0Gqtgfk9ABCwf3dv737GMfICTNQ2bTu6ubQEaZo7GS6k7NxqNNgqlUImauc5Mt80Pa/7ZWPjiJiXDSFuH7RaawBgCHGHiUgr9S1zNdUBAKg03Qp8f3jdcarSNHeElJ0bjlP+6fthmiSb4/sTAk3P62qljoiZQbRAQtSLrk8VyLuQllWqOY5ddH0ig6waz5ev/Pl1Jq2rJkCEyu95aV6zZuB5E7s0Dgw/PXsJoifWXM02qjMAAB2eIgmCiDXvlu+92CwUiPafPiIhXtvOvE1i1ByrFLHfj1knjysrnbfTMxBG25qrXZKHhz0MD3vnl4TExcwtzkCrhcw2AJizsyNjo1oFUl0fwcYz+G/xJDQiQIb4qsPTyz4ZDJAMBv8MRiEgjF6hAGvtJkEQsUoBAOX6Isr1xfOZSnEW/IhYabdQoLKy/YY178Z+P1Ynx2CtwFpBnRwj/t6PoPSryur2uxHX016NPm49JCFdVqp+8VqPlXbHyQDwF+sF0G8ENFzgAAAAAElFTkSuQmCC

		// 2 /static/img/yp.png
		// data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAACBklEQVQ4jX2Ry2sTURTGvzP3TpyYJiWFIBG0hepIQ2ilCcW4iJUGm4Xgyk3RveA2dpvsXDTgTtwKov0DFHxMMWZhpcYH1BCcWmi7UBrQltoJhcy910VNmbx6Vveex++c7xyCxz7MzLxTQBoAQHQ1ZVklAFjOZKah1FsAIKB8aWnpSqtG8wIUUV43DOfMxAS4rhdbfsb5wtnxceXz+/cVUd5bw72flGWVVrLZGimV0Bi7sJzJTAOAxtiYIiIpxPfWVD0nAADhuvd+2XbjlGkOcF0vMs4XoqZ5ctu2991mM9eZ3wVIWVZJClEjpRSIYsRYvF/3ngDvFNzn8582TaNf964dtMzO3R8c2PnND0Jh1IhwYuoPdwYjQViJrlzqdCy++fJAge6YIxEjHAwAAHb3GljbqjtCykdz1yZzfQGPX3684WP64lR82NCYBo0Ow1IpSCGx8m3zQEhx89Zs4nlPCT7G8+ZIxHj/db2XMoyNRg17Y7sA4AjQtkQpZCwcDCCdNAEA6aTZ9h4KBaAk4t6anlc43lTbr02Cxqi6u9eYrK7/BACUK/ZRrFyxETsXhcZotS+g2VSFta36s8sXRwOcs7ZOritQqW44rpAFr7/rjE9ffy4SaXfPD0eModDhGXf+OvixWXeEkA/nZhPzxwIA4MmrT9c1ooKSKv5f2qorZOF2NvmiM/cf7sTJP/6yuMUAAAAASUVORK5CYII=
		
		// 3 
		// data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAACDUlEQVQ4jXWSz2sTQRTHv5OZye5WSyGBiIIectjU2qRSKTQHY+vmUKEYEARRvPsH1JNg1ouXFrwV/4FqToJS8bKxMZeYniRRYvZQEBV/9MepqTazk+mhpGySzbvNd977vPe+MwS++GhZHxSQAQAQMp92nBIAVLLZOSi1AQAEKM8Wi9e6NSE/QBGS57reOj81Bcb5SlenjC1fSKVU2DD2FSF5fw3zH9KOU9pcWGgQpa6EKE1Ustk5AAhRelERQjpSNrtTBU4AANLzHv5y3YMzpnmacb5CGVs+a5ojf1x33xNiqT9/AJB2nFJHygZRSoGQCULp5LDugQD/FCwcNs6Zpj6s+4AH3di+GhvTDhU71Ai+4y/0y2PsgI+OwhnMJf3C+tP7zyhCD1LjCT0WjR4D93ZRb7otIcXzxUdrS0MBb57czXHOC9fTs3qY8x5wWwi8r1T+/2vL27fstfXAFRin+dR4Qn9XLgdthplkUv/0tWEDOAH0mOhJORGLRpGzLABAIh5HIh4HAOQsC7FIBNKTk/6awFcYFipA612B8i/be7vTm7U6AKC5tXVy97pYxEwyCcpofSigLdp2vem+vJHJnAoycaNabXlC2H6d+g+F0mf3zvylyLcfP6dHDIMZmoZOp4PfOzuo1motIbzVm49frPprBv4BALyy7y1q4ZAt5bFhlNK6J4Sdyxfe9uceAQ5kzAxUmf+UAAAAAElFTkSuQmCC
		
		ListItem.nodesCountList.get(0).setTopStyle("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAAB4ElEQVQ4jX1QTWvUUBQ9N+8lZD6KiFBxiu4asUyni24cpLXSWXQhuPI/+ANaxI2TlVCm4E76E9yruxTHKlTcTqWYcW1wQKRtMsQm710XbWrmI97dPfeec889hFx9Xl//wMAqAIDoQdPzugBw0Gqtgfk9ABCwf3dv737GMfICTNQ2bTu6ubQEaZo7GS6k7NxqNNgqlUImauc5Mt80Pa/7ZWPjiJiXDSFuH7RaawBgCHGHiUgr9S1zNdUBAKg03Qp8f3jdcarSNHeElJ0bjlP+6fthmiSb4/sTAk3P62qljoiZQbRAQtSLrk8VyLuQllWqOY5ddH0ig6waz5ev/Pl1Jq2rJkCEyu95aV6zZuB5E7s0Dgw/PXsJoifWXM02qjMAAB2eIgmCiDXvlu+92CwUiPafPiIhXtvOvE1i1ByrFLHfj1knjysrnbfTMxBG25qrXZKHhz0MD3vnl4TExcwtzkCrhcw2AJizsyNjo1oFUl0fwcYz+G/xJDQiQIb4qsPTyz4ZDJAMBv8MRiEgjF6hAGvtJkEQsUoBAOX6Isr1xfOZSnEW/IhYabdQoLKy/YY178Z+P1Ynx2CtwFpBnRwj/t6PoPSryur2uxHX016NPm49JCFdVqp+8VqPlXbHyQDwF+sF0G8ENFzgAAAAAElFTkSuQmCC");
		ListItem.nodesCountList.get(1).setTopStyle("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAACBklEQVQ4jX2Ry2sTURTGvzP3TpyYJiWFIBG0hepIQ2ilCcW4iJUGm4Xgyk3RveA2dpvsXDTgTtwKov0DFHxMMWZhpcYH1BCcWmi7UBrQltoJhcy910VNmbx6Vveex++c7xyCxz7MzLxTQBoAQHQ1ZVklAFjOZKah1FsAIKB8aWnpSqtG8wIUUV43DOfMxAS4rhdbfsb5wtnxceXz+/cVUd5bw72flGWVVrLZGimV0Bi7sJzJTAOAxtiYIiIpxPfWVD0nAADhuvd+2XbjlGkOcF0vMs4XoqZ5ctu2991mM9eZ3wVIWVZJClEjpRSIYsRYvF/3ngDvFNzn8582TaNf964dtMzO3R8c2PnND0Jh1IhwYuoPdwYjQViJrlzqdCy++fJAge6YIxEjHAwAAHb3GljbqjtCykdz1yZzfQGPX3684WP64lR82NCYBo0Ow1IpSCGx8m3zQEhx89Zs4nlPCT7G8+ZIxHj/db2XMoyNRg17Y7sA4AjQtkQpZCwcDCCdNAEA6aTZ9h4KBaAk4t6anlc43lTbr02Cxqi6u9eYrK7/BACUK/ZRrFyxETsXhcZotS+g2VSFta36s8sXRwOcs7ZOritQqW44rpAFr7/rjE9ffy4SaXfPD0eModDhGXf+OvixWXeEkA/nZhPzxwIA4MmrT9c1ooKSKv5f2qorZOF2NvmiM/cf7sTJP/6yuMUAAAAASUVORK5CYII=");
		ListItem.nodesCountList.get(2).setTopStyle("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QA/wD/AP+gvaeTAAACDUlEQVQ4jXWSz2sTQRTHv5OZye5WSyGBiIIectjU2qRSKTQHY+vmUKEYEARRvPsH1JNg1ouXFrwV/4FqToJS8bKxMZeYniRRYvZQEBV/9MepqTazk+mhpGySzbvNd977vPe+MwS++GhZHxSQAQAQMp92nBIAVLLZOSi1AQAEKM8Wi9e6NSE/QBGS57reOj81Bcb5SlenjC1fSKVU2DD2FSF5fw3zH9KOU9pcWGgQpa6EKE1Ustk5AAhRelERQjpSNrtTBU4AANLzHv5y3YMzpnmacb5CGVs+a5ojf1x33xNiqT9/AJB2nFJHygZRSoGQCULp5LDugQD/FCwcNs6Zpj6s+4AH3di+GhvTDhU71Ai+4y/0y2PsgI+OwhnMJf3C+tP7zyhCD1LjCT0WjR4D93ZRb7otIcXzxUdrS0MBb57czXHOC9fTs3qY8x5wWwi8r1T+/2vL27fstfXAFRin+dR4Qn9XLgdthplkUv/0tWEDOAH0mOhJORGLRpGzLABAIh5HIh4HAOQsC7FIBNKTk/6awFcYFipA612B8i/be7vTm7U6AKC5tXVy97pYxEwyCcpofSigLdp2vem+vJHJnAoycaNabXlC2H6d+g+F0mf3zvylyLcfP6dHDIMZmoZOp4PfOzuo1motIbzVm49frPprBv4BALyy7y1q4ZAt5bFhlNK6J4Sdyxfe9uceAQ5kzAxUmf+UAAAAAElFTkSuQmCC");
		logger.info("刷新每小时用户笔记数量排名结束");
	}
	
	/**
	 * 每小时清理过滤器
	 */
	@Scheduled(fixedRate = 3600000)
	public void SessionInterceptorAdapter() {
		logger.info("每小时清理访问者过滤器开始");
		cn.bowseros.config.SessionInterceptorAdapter.filterRefresh();
		logger.info("每小时清理访问者过滤器结束");
	}
}
