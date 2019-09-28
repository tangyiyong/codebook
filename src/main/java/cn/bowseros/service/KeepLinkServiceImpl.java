package cn.bowseros.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.bowseros.entity.KeepLink;
import cn.bowseros.entity.People;
import codebook.commons.R;
import codebook.commons.ValueUtils;

/**
 *
 * Created by bowser Sep 10, 2019/10:24:27 PM
 * 
 */
@Service
public class KeepLinkServiceImpl extends BaseService implements KeepLinkService{
	
	@Transactional
	@Override
	public R<String> update(KeepLink kl) {
		People p = (People) session.getAttribute("user_");
		if (p == null) {
			return new R<>(766, "未登录");
		}
		
		String userId = p.getId();
		int r = 0;
		if (ValueUtils.isEmpty(kl.getId())) {
			kl.setId(String.valueOf(gid.nextId()));
			kl.setUserid(userId);
			r = keepLinkMapper.insert(kl);
		}else {
			r = keepLinkMapper.updateById(kl);
		}
		return r > 0 ? R.SUCCESS : R.FAIL;
	}

	@Transactional
	@Override
	public R<String> delete(KeepLink kl) {
		People p = (People) session.getAttribute("user_");
		if (p == null) {
			return new R<>(766, "未登录");
		}
		if (kl == null) {
			return new R<>(799, "参数非法");
		}
		if (!kl.getUserid().equalsIgnoreCase(p.getId())) {
			return new R<>(710, "找不到数据");
		}
		int r = keepLinkMapper.deleteById(kl);
		return r > 0 ? R.SUCCESS : R.FAIL;
	}
	
	@Override
	public R<List<KeepLink>> mykeeplinks() {
		People p = (People) session.getAttribute("user_");
		if (p == null) {
			return new R<>(766, "未登录");
		}
		
		QueryWrapper<KeepLink> queryWrapper = new QueryWrapper<KeepLink>();
		queryWrapper.eq("userid",p.getId());
		queryWrapper.orderByDesc("id");
		List<KeepLink> klIst = keepLinkMapper.selectList(queryWrapper);
		
		p = peopleMapper.selectById(p.getId());
		Map<String,Object> systemSetting = new HashMap<String, Object>();
		systemSetting.put("allowdelete", p.getAllowdelete());
		return new R<List<KeepLink>>(klIst,systemSetting);
	}

	
	
	
}
