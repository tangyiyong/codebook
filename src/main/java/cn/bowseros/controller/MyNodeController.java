package cn.bowseros.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bowseros.entity.KeepLink;
import cn.bowseros.entity.NodebookItem;
import codebook.commons.R;

/**
 *
 * Created by bowser Sep 23, 2019/5:39:24 PM
 * 我的节点
 */
@Controller
@RequestMapping("/i")
public class MyNodeController extends BaseController{

	@GetMapping("/node")
	@ResponseBody
	public R<Map<String,Object>> node(){
		//笔记节点
		R<List<NodebookItem>> mynodes = nodebookItemService.mynodes();
		//链接节点
		R<List<KeepLink>> mylinks = keepLinkService.mykeeplinks();
		
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("mynodes", mynodes);
		result.put("mylinks", mylinks);
		return new R<Map<String,Object>>(result);
	}
}
