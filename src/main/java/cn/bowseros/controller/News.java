package cn.bowseros.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by bowser Sep 16, 2019/2:33:09 PM
 * 
 */
@RestController
@RequestMapping("/news")
public class News extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/get/{start}/{channel}")
	public List<Map<String, Object>> get(@PathVariable("start") int start, @PathVariable("channel") String channel) {

		List<Map<String, Object>> newsArray = new ArrayList<Map<String, Object>>();
		String appKey = "eebcbcfe9c33abd28b81ea47aaa15bb9";
		int num = 5;

		try {
			Map<String, Object> m = restTemplate.getForObject("https://way.jd.com/jisuapi/get?channel=" + channel + "&num=" + num + "&start=" + start + "&appkey=" + appKey + "", Map.class);
			String code = (String) m.get("code");
			if (!code.equals("10000")) {
				return newsArray;
			}
			Map<String, Object> result = (Map<String, Object>) m.get("result");
			Map<String, Object> data = (Map<String, Object>) result.get("result");
			List<Map<String, Object>> listMap = (List<Map<String, Object>>) data.get("list");
			for (Map<String, Object> map : listMap) {
				map.put("content", null);
				map.put("url", null);
				newsArray.add(map);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return newsArray;
	}

//	public static void main(String[] args) {
//		RestTemplate rt = new RestTemplate();
//		Map m = rt.getForObject("https://way.jd.com/jisuapi/get?channel=头条&num=10&start=0&appkey=eebcbcfe9c33abd28b81ea47aaa15bb9", Map.class);
//		System.out.println(m.get("code"));
//		System.out.println(m.get("msg"));
//		
//		Map result = (Map) m.get("result");
//		System.out.println(result.get("status"));
//		System.out.println(result.get("msg"));
//		
//		Map data = (Map) result.get("result");
//		System.out.println(data.get("channel"));
//		System.out.println(data.get("num"));
//		
//		List<Map<String,Object>> newsArray = new ArrayList<Map<String,Object>>();
//		
//		List<Map> listMap = (List<Map>) data.get("list");
//		for (Map map : listMap) {
//			map.put("content", null);
//			newsArray.add(map);
//		}
//		listMap = null;
//		
//		System.out.println(newsArray);
//	}
}
