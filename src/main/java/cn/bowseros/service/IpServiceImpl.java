package cn.bowseros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

import cn.bowseros.vo.IpInfo;

/**
 *
 * Created by bowser Sep 27, 2019/2:43:49 PM
 * 
 */
@Service
public class IpServiceImpl extends BaseService implements IpService{
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public IpInfo getIpInfo(String ip) {
		String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip;
		String ipinfostr = restTemplate.getForObject(url, String.class);
		return JSON.parseObject(ipinfostr,IpInfo.class);
	}
	

}
