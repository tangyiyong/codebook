package cn.bowseros.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bowseros.entity.NodebookItem;
import cn.bowseros.entity.People;
import cn.bowseros.vo.NodebookVo;
import codebook.commons.R;

/**
 *
 * Created by bowser Sep 7, 2019/3:11:59 PM
 * 
 */
@Controller
@RequestMapping("/nodebook")
public class NodebookController extends BaseController{
	
	/**
	 * 跳转个人设置页
	 * @return
	 */
	@RequestMapping("/mysetting")
	public String mysetting() {
		People p = (People) session.getAttribute("user_");
		if (p != null) {
			return "mysetting";
		}
		return "login";
	}
	
	/**
	 * 获取个人设置信息
	 * @return
	 */
	@GetMapping("/mysetting/get")
	@ResponseBody
	public R<People> myInfoget() {
		return peopleService.myInfoget();
	}
	
	/**
	 * 更新个人设置
	 * @param p
	 * @return
	 */
	@PostMapping("/mysetting/update")
	@ResponseBody
	public R<String> myInfoupdate(People p) {
		return peopleService.update(p);
	}
	
	/**
	 * 更新系统设置
	 * @param p
	 * @return
	 */
	@PostMapping("/setting/update")
	@ResponseBody
	public R<String> settingUpdate(People p) {
		return peopleService.settingUpdate(p);
	}
	
	/**
	 * 更新背景图
	 * @param p
	 * @return
	 */
	@PostMapping("/mysetting/background/update")
	@ResponseBody
	public R<String> updateBackground(People p) {
		return peopleService.updateBackground(p);
	}
	
	/**
	 * 更新登陆密码
	 * @param p
	 * @return
	 */
	@PostMapping("/mysetting/passcode/update")
	@ResponseBody
	public R<String> updatePassword(People p) {
		return peopleService.updatePassword(p);
	}
	
	/**
	 * 邮箱验证
	 * @return
	 */
	@PostMapping("/mysetting/mail/validation")
	@ResponseBody
	public R<String> mailValidation() {
		return peopleService.mailValidation();
	}
	
	/**
	 * 跳转新增笔记页面
	 * @param nodebookContent
	 * @return
	 */
	@RequestMapping("/new")
	public String insert(){
		People p = (People) session.getAttribute("user_");
		if (p != null) {
			return "edit";
		}
		return "redirect:/login";
	}
	
	/**
	 * 提交新增笔记
	 * @param nodebookContent
	 * @return
	 */
	@PostMapping("/insert")
	@ResponseBody
	public R<String> insert(NodebookVo nodebookVo){
		return nodebookItemService.insert(nodebookVo);
	}
	
	/**
	 * 提交新增笔记分组
	 * @param nodebookContent
	 * @return
	 */
	@PostMapping("/insert/group")
	@ResponseBody
	public R<String> insertGroup(NodebookItem nodebookItem){
		return nodebookItemService.insertGroup(nodebookItem);
	}
	
	/**
	 * 查询我的笔记节点
	 * @return
	 */
	@GetMapping("/mynodes")
	@ResponseBody
	public R<List<NodebookItem>> mynodes(){
		return nodebookItemService.mynodes();
	}
	
	/**
	 * 查询我的笔记分组
	 * @return
	 */
	@GetMapping("/mynodes/group")
	@ResponseBody
	public R<List<Map<String,Object>>> mynodesGroup(){
		return nodebookItemService.mynodesGroup();
	}
	
	/**
	 * 编辑笔记
	 * @param model
	 * @param nodebookid
	 * @return
	 */
	@RequestMapping("/edit/{nodebookid}")
	public String edit(Model model,@PathVariable("nodebookid")String nodebookid) {
		model.addAttribute("nodebookid", nodebookid);
		return "edit";
	}
	
	/**
	 * 跳转查看笔记页
	 * @param model
	 * @param nodebookname
	 * @param nodebookid
	 * @return
	 */
	@RequestMapping("/view/{nodebookid}")
	public String index(Model model,@PathVariable("nodebookid")String nodebookid) {
		model.addAttribute("nodebookid", nodebookid);
		return "codebook";
	}
	
	/**
	 * 查询相关笔记
	 * @param model
	 * @param nodebookname
	 * @param nodebookid
	 * @return
	 */
	@GetMapping("/matter")
	@ResponseBody
	public R<List<NodebookVo>> matter(String data) {
		return nodebookItemService.matter(data);
	}
	
	/**
	 * 查询我的笔记内容
	 * @return
	 */
	@GetMapping("/content/{nodebookid}")
	@ResponseBody
	public R<NodebookVo> mynodesContent(@PathVariable("nodebookid")String nodebookid){
		return nodebookItemService.mynodesContent(nodebookid);
	}
	
	/**
	 * 删除笔记
	 * @param nodebookContent
	 * @return
	 */
	@PostMapping("/delete")
	@ResponseBody
	public R<String> delete(NodebookVo nodebookVo){
		return nodebookItemService.delete(nodebookVo);
	}
}
