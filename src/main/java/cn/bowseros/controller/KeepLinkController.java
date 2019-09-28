package cn.bowseros.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.bowseros.entity.KeepLink;
import codebook.commons.R;

/**
 *
 * Created by bowser Sep 10, 2019/10:20:51 PM
 * 
 */
@Controller
@RequestMapping("/keeplink")
public class KeepLinkController extends BaseController {

	@PostMapping("/update")
	@ResponseBody
	public R<String> update(KeepLink kl){
		return keepLinkService.update(kl);
	}

	@PostMapping("/delete")
	@ResponseBody
	public R<String> delete(KeepLink kl){
		return keepLinkService.delete(kl);
	}

	@GetMapping("/mykeeplinks")
	@ResponseBody
	public R<List<KeepLink>> mykeeplinks(){
		return keepLinkService.mykeeplinks();
	}

}
