package cn.bowseros.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bowseros.entity.People;

/**
 *
 * Created by bowser Sep 7, 2019/4:30:45 PM
 * 
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
	
//	@ResponseBody
//	@GetMapping("/ip")
//	public IpInfo ip(String ip) {
//		return ipService.getIpInfo(ip);
//	}
	
	@RequestMapping("")
	public String index() {
		People p = (People) session.getAttribute("user_");
		if (p != null) {
			return "redirect:/";
		}
		return "login";
	}
	
	/**
	 * 跳转至用户创建页
	 * @return
	 */
	@RequestMapping("/create")
	public String create() {
		return "create";
	}
	
	/**
	 * 退出登录
	 * @return
	 */
	@GetMapping("/out")
	public String out() {
		session.removeAttribute("user_");
		return "redirect:/";
	}
	
	/**
	 * 创建用户
	 * @param people
	 * @return
	 */
	@PostMapping("/newpeople")
	@ResponseBody
	public String newPeople(People people) {
		return peopleService.newPeople(people);
	}
	
	/**
	 * 登陆检查
	 * @param people
	 * @return
	 */
	@PostMapping("/check")
	@ResponseBody
	public String check(People people) {
		People p = (People) session.getAttribute("user_");
		if (p != null) {
			return "success";
		}
		return peopleService.check(people);
	}
}
