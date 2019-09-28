package cn.bowseros.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.bowseros.entity.People;

/**
 *
 * Created by bowser Sep 27, 2019/1:57:40 PM
 * 话题
 */
@Controller
@RequestMapping("/topic")
public class TopicController extends BaseController {

	@RequestMapping("")
	public String index(Model model) {
		return rindex(model);
	}

	@RequestMapping("/")
	public String rindex(Model model) {
		People p = (People) session.getAttribute("user_");
		String page = "topic";
		if (p == null) {
			page = "login";
		}
		return page;
	}
}
