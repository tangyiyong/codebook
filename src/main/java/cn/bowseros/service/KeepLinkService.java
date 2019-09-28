package cn.bowseros.service;

import java.util.List;

import cn.bowseros.entity.KeepLink;
import codebook.commons.R;

/**
 *
 * Created by bowser Sep 10, 2019/10:24:14 PM
 * 
 */
public interface KeepLinkService {
	
	public R<String> update(KeepLink kl);
	
	public R<String> delete(KeepLink kl);
	
	public R<List<KeepLink>> mykeeplinks();

}
