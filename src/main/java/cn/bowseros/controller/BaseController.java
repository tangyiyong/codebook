package cn.bowseros.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import cn.bowseros.config.cache.DbStore;
import cn.bowseros.service.IpService;
import cn.bowseros.service.KeepLinkService;
import cn.bowseros.service.NodebookItemService;
import cn.bowseros.service.PeopleService;

/**
 *
 * Created by bowser Sep 7, 2019/2:37:06 PM
 * 
 */
public class BaseController {

	@Autowired
	protected NodebookItemService nodebookItemService;
	@Autowired
	protected PeopleService peopleService;
	@Autowired
	protected KeepLinkService keepLinkService;
	@Autowired
	protected HttpSession session;
	@Autowired
	protected HttpServletRequest request;
	/** 邮箱服务 */
	@Value("${spring.mail.username}")
	protected String efrom;
	@Autowired
	protected JavaMailSender mailSender;
	@Autowired
	protected RestTemplate restTemplate;
	@Autowired
	protected DbStore dbStore;
	@Autowired
	protected IpService ipService;
	protected int querySize = 30;
}
