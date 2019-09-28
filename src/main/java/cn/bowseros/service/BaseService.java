package cn.bowseros.service;
/**
 *
 * Created by bowser Sep 7, 2019/3:09:10 PM
 * 
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import cn.bowseros.config.cache.DbStore;
import cn.bowseros.mapper.KeepLinkMapper;
import cn.bowseros.mapper.NodebookContentMapper;
import cn.bowseros.mapper.NodebookItemMapper;
import cn.bowseros.mapper.PeopleMapper;
import codebook.commons.SnowflakeIdWorker;

public class BaseService {
	
	protected String adminEmail = "bowserostree@163.com";
	
	@Autowired
	protected NodebookContentMapper nodebookContentMapper;
	@Autowired
	protected NodebookItemMapper nodebookItemMapper;
	@Autowired
	protected PeopleMapper peopleMapper;
	@Autowired
	protected KeepLinkMapper keepLinkMapper;
	@Autowired
	protected HttpSession session;
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected DbStore dbStore;
	protected SnowflakeIdWorker gid = new SnowflakeIdWorker(0, 0);
}
