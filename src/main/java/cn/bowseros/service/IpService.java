package cn.bowseros.service;

import cn.bowseros.vo.IpInfo;

/**
 *
 * Created by bowser Sep 27, 2019/2:36:29 PM
 * 
 */
public interface IpService {

	// http://ip.taobao.com/service/getIpInfo.php?ip=66.249.75.52
	// {"code":0,"data":{"ip":"66.249.75.52","country":"美国","area":"","region":"艾奥瓦","city":"XX","county":"XX","isp":"谷歌","country_id":"US","area_id":"","region_id":"US_115","city_id":"xx","county_id":"xx","isp_id":"3000519"}}
	
	IpInfo getIpInfo(String ip);
}
