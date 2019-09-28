package cn.bowseros.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.bowseros.entity.People;
import cn.bowseros.vo.NodesCount;

/**
 *
 * Created by bowser Sep 7, 2019/2:37:06 PM
 * 
 */
@Controller
public class PageController extends BaseController {

	/**
	 * 访问主页 已登陆 => mynodes/index 未登陆 => sharenodes/index
	 * 
	 * @return
	 */
	@RequestMapping("")
	public String index(Model model) {
		return rindex(model);
	}

	@RequestMapping("/")
	public String rindex(Model model) {
		People p = (People) session.getAttribute("user_");

		String page = "sharenodes/index";
		if (p != null) {
			// 查询已登陆用户的笔记数量和背景图
			Map<String, String> myNum = nodebookItemService.mynodesNum();
			model.addAttribute("ubackground", myNum.get("ubackground"));
			model.addAttribute("groupCount", myNum.get("groupCount"));
			model.addAttribute("nodeCount", myNum.get("nodeCount"));
			page = "mynodes/index";
		}

		// 笔记排名
		List<NodesCount> nodesTop = nodebookItemService.nodebookCount();
		model.addAttribute("nodesTop", nodesTop);

		// 共享笔记
		Map<String, Object> shareNodes = nodebookItemService.getOpenshare(1, querySize);
		model.addAttribute("shareNodes", shareNodes);

		// 开发者笔记总条数
		Integer nodeCount = nodebookItemService.nodeCount();
		model.addAttribute("nodeTotal", nodeCount);

		model.addAttribute("currentpage", 1);
		model.addAttribute("nextPage", 2);
		model.addAttribute("perPage", 1);
		return page;
	}

	@RequestMapping("/about")
	public String about() {
		return "version/home";
	}

	/**
	 * 访问拒绝
	 * 
	 * @return
	 */
	@RequestMapping("/access/rejection")
	public String accessRejection() {
		return "error/accessrejection";
	}

}
