package cn.bowseros.controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.bowseros.config.ListItem;
import cn.bowseros.vo.Ucode;
import codebook.commons.R;
import codebook.commons.SHA;
import codebook.commons.ValueUtils;

/**
 *
 * Created by bowser Sep 18, 2019/12:56:29 PM
 * 
 */
@Controller
@RequestMapping("/mails")
public class Mail extends BaseController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 邀请码获取
	 * @param ucode
	 * @return
	 */
	@PostMapping("/getucode")
	@ResponseBody
	public R<String> mailCount(Ucode ucode) {
		String ip_ = request.getRemoteAddr();
		if (ListItem.toDayRegisterList != null) {
			if (ListItem.toDayRegisterList.contains(ip_)) {
				return new R<>(123, "抱歉,最近一小时内您已经注册过了!");
			}
		}
		
		String username = ucode.getInu();
		String email = ucode.getIne();
		
		// 针对v2ex
		if (email.equalsIgnoreCase("hello@v2ex.com") || email.contains("v2ex.com")) {
			return new R<>(500, "抱歉,我们无法为"+email+"提供邀请码!");
		}
		
		if(ValueUtils.isEmpty(username) || ValueUtils.isEmpty(email)) {
			return new R<>(500, "非法参数");
		}
		if (peopleService.exits(username)) {
			return new R<>(500, "用户名不可用"); 
		}
		if (peopleService.exitsEmail(email)) {
			return new R<>(500, "邮箱地址已被占用"); 
		}
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
	    MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, true);
			helper.setFrom(efrom);
			helper.setTo(email);
			helper.setSubject("tuna技术分享-代码笔记邀请码");
			
			String ucode_ = SHA.yqCode(username);
			String text = 	"<p>用户名: "+ username +"</p>"+
							"<p>邀请码: " + ucode_+"</p>";
			helper.setText("<html><body>"+text+"</body></html>", true);
			
			mailSender.send(mimeMessage);
			logger.info("已发送邀请码邮件 {} {}",ucode_, JSON.toJSONString(ucode));
		} catch (MessagingException e) {
			logger.error(e.getMessage(),e);
			return new R<>(500, "掉链子了,请重试 ~");
		}
		
		return R.SUCCESS;
	}

}