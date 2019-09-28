package cn.bowseros.service;

import cn.bowseros.entity.People;
import codebook.commons.R;

/**
 *
 * Created by bowser Sep 7, 2019/4:38:44 PM
 * 
 */
public interface PeopleService {
	/**
	 * 用户是否存在
	 * @param id
	 * @return
	 */
	boolean exitsId(String id);
	
	/**
	 * 用户名是否存在
	 * @param username
	 * @return
	 */
	boolean exits(String username);
	
	/**
	 * 邮箱是否存在
	 * @param username
	 * @return
	 */
	boolean exitsEmail(String email);
	
	/**
	 * 校验用户
	 * @param people
	 * @return
	 */
	String check(People people);
	
	/**
	 * 创建用户
	 * @param people
	 * @return
	 */
	String newPeople(People people);

	/**
	 * 更新个人信息
	 * @return
	 */
	R<String> update(People p);

	/**
	 * 获取个人信息
	 * @return
	 */
	R<People> myInfoget();

	/**
	 * 邮箱验证
	 * @return
	 */
	R<String> mailValidation();

	/**
	 * 更新密码
	 * @param p
	 * @return
	 */
	R<String> updatePassword(People p);

	/**
	 * 系统设置更新
	 * @param p
	 * @return
	 */
	R<String> settingUpdate(People p);

	/**
	 * weibo登陆用户数据同步
	 * @param p
	 * @return
	 */
	R<String> weiboNewPeople(People p);

	/**
	 * 获取用户信息
	 * @param id
	 * @return
	 */
	People loadInfo(String id);
	
	/**
	 * 更新背景图
	 * @param p
	 * @return
	 */
	R<String> updateBackground(People p);

}
