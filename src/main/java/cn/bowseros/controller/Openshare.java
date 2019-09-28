package cn.bowseros.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bowseros.entity.People;
import cn.bowseros.vo.NodesCount;

/**
 *
 * Created by bowser Sep 16, 2019/2:33:09 PM
 * 
 */
@Controller
@RequestMapping("/share")
public class Openshare extends BaseController {

	/**
	 * 不同类型节点的共享笔记
	 * 
	 * @return
	 */
	@RequestMapping("/nodes/t/{nodetype}")
	public String currentpageByType(Model model, @PathVariable("nodetype") String nodetype) {
		return currentpageByType(model, nodetype, 1);
	}

	/**
	 * 不同类型节点的共享笔记 分页查询
	 * 
	 * @return
	 */
	@RequestMapping("/nodes/t/{nodetype}/{currentpage}")
	public String currentpageByType(Model model, @PathVariable("nodetype") String nodetype,
			@PathVariable("currentpage") int currentpage) {
		// 笔记排名
		List<NodesCount> nodesTop = nodebookItemService.nodebookCount();
		model.addAttribute("nodesTop", nodesTop);

		// 共享笔记
		Map<String, Object> shareNodes = nodebookItemService.getOpenshareByType(nodetype, currentpage, querySize);
		model.addAttribute("shareNodes", shareNodes);

		// 开发者笔记总条数
		Integer nodeCount = nodebookItemService.nodeCount();
		model.addAttribute("nodeTotal", nodeCount);

		model.addAttribute("currentpage", currentpage);
		model.addAttribute("nextPage", currentpage + 1);
		model.addAttribute("perPage", currentpage - 1);
		model.addAttribute("typeQuery", "t/" + nodetype + "/");// 节点类型查询需要讲类型传到前面

		People p = (People) session.getAttribute("user_");
		if (p != null) {
			// 查询已登陆用户的笔记数量和背景图
			Map<String, String> myNum = nodebookItemService.mynodesNum();
			model.addAttribute("ubackground", myNum.get("ubackground"));
			model.addAttribute("groupCount", myNum.get("groupCount"));
			model.addAttribute("nodeCount", myNum.get("nodeCount"));
			return "mynodes/index";
		}
		return "sharenodes/index";
	}

	/**
	 * 共享笔记的当前页数据
	 * 
	 * @return
	 */
	@RequestMapping("/nodes/{currentpage}")
	public String currentpage(Model model, @PathVariable("currentpage") int currentpage) {
		currentpage = currentpage < 1 ? 1 : currentpage;

		// 笔记排名
		List<NodesCount> nodesTop = nodebookItemService.nodebookCount();
		model.addAttribute("nodesTop", nodesTop);

		// 共享笔记
		Map<String, Object> shareNodes = nodebookItemService.getOpenshare(currentpage, querySize);
		model.addAttribute("shareNodes", shareNodes);

		// 开发者笔记总条数
		Integer nodeCount = nodebookItemService.nodeCount();
		model.addAttribute("nodeTotal", nodeCount);

		model.addAttribute("currentpage", currentpage);
		model.addAttribute("nextPage", currentpage + 1);
		model.addAttribute("perPage", currentpage - 1);

		People p = (People) session.getAttribute("user_");
		if (p != null) {
			// 查询已登陆用户的笔记数量和背景图
			Map<String, String> myNum = nodebookItemService.mynodesNum();
			model.addAttribute("ubackground", myNum.get("ubackground"));
			model.addAttribute("groupCount", myNum.get("groupCount"));
			model.addAttribute("nodeCount", myNum.get("nodeCount"));
			return "mynodes/index";
		}
		return "sharenodes/index";
	}

	/**
	 * 获取共享笔记
	 * 
	 * @return
	 */
	@GetMapping("/get")
	@ResponseBody
	public Map<String, Object> get(long current) {
		return nodebookItemService.getOpenshare(current, 30);
	}

	/**
	 * 笔记数量排名
	 * 
	 * @return
	 */
	@GetMapping("/nodebook/count")
	@ResponseBody
	public List<NodesCount> nodebookCount() {
		return nodebookItemService.nodebookCount();
	}

	/**
	 * 跳转查看笔记页
	 * 
	 * @param model
	 * @param nodebookname
	 * @param nodebookid
	 * @return
	 */
	@RequestMapping("/view/{nodebookid}")
	public String index(Model model, @PathVariable("nodebookid") String nodebookid) {
		model.addAttribute("nodebookid", nodebookid);
		return "sharecodebook";
	}
}
