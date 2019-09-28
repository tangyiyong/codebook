package cn.bowseros.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import codebook.commons.DateTimeUtils;

/**
 *
 * Created by bowser Sep 7, 2019/4:36:30 PM
 * 
 */
@TableName("people")
public class People implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	private String uname;// 用户名
	private String myemail;// 邮箱
	private String myhome;// 个人网站
	private String mygitee;// 码云
	private String mygithub;// github
	private String passcode;// 口令
	private Date createTime;// 创建时间
	private int emailvalidation;// 邮箱验证(1=验证,0=未验证)
	private int openshare;// 是否共享笔记(0=否,1=是)
	private int allowdelete;// 允许删除操作(0=不允许,1=允许)
	private int loginReminder;// 登陆提醒(0=不提醒,1=提醒)
	private int upass;// 通行证 (0=正常,1=异常)(默认为0)
	private String upassmessage;// 通行证消息(主要在通行证异常时记录消息)
	private String uavatar;//头像
	private String ubackground;//背景图
	
	@TableField(exist = false)
	private String ucode;// 邀请码
	@TableField(exist = false)
	private String uemail;// 邮箱
	@TableField(exist = false)
	private String oldpasscode;// 旧密码
	@TableField(exist = false)
	private String newpasscode;// 新密码
	@TableField(exist = false)
	private String registerTime;// 注册时间
	@TableField(exist = false)
	private int nodeCount;// 笔记数量
	
	
	public String getUbackground() {
		return ubackground;
	}

	public void setUbackground(String ubackground) {
		this.ubackground = ubackground;
	}

	public String getUavatar() {
		return uavatar;
	}

	public void setUavatar(String uavatar) {
		this.uavatar = uavatar;
	}

	public String getUpassmessage() {
		return upassmessage;
	}

	public void setUpassmessage(String upassmessage) {
		this.upassmessage = upassmessage;
	}

	public int getUpass() {
		return upass;
	}

	public void setUpass(int upass) {
		this.upass = upass;
	}

	public int getLoginReminder() {
		return loginReminder;
	}

	public void setLoginReminder(int loginReminder) {
		this.loginReminder = loginReminder;
	}

	public String getUemail() {
		return uemail;
	}

	public void setUemail(String uemail) {
		this.uemail = uemail;
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	public String getRegisterTime() {
		return DateTimeUtils.getYearMonthDay(createTime);
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getOldpasscode() {
		return oldpasscode;
	}

	public void setOldpasscode(String oldpasscode) {
		this.oldpasscode = oldpasscode;
	}

	public String getNewpasscode() {
		return newpasscode;
	}

	public void setNewpasscode(String newpasscode) {
		this.newpasscode = newpasscode;
	}

	public int getOpenshare() {
		return openshare;
	}

	public void setOpenshare(int openshare) {
		this.openshare = openshare;
	}

	public int getAllowdelete() {
		return allowdelete;
	}

	public void setAllowdelete(int allowdelete) {
		this.allowdelete = allowdelete;
	}

	public int getEmailvalidation() {
		return emailvalidation;
	}

	public void setEmailvalidation(int emailvalidation) {
		this.emailvalidation = emailvalidation;
	}

	public String getMyemail() {
		return myemail;
	}

	public void setMyemail(String myemail) {
		this.myemail = myemail;
	}

	public String getMyhome() {
		return myhome;
	}

	public void setMyhome(String myhome) {
		this.myhome = myhome;
	}

	public String getMygitee() {
		return mygitee;
	}

	public void setMygitee(String mygitee) {
		this.mygitee = mygitee;
	}

	public String getMygithub() {
		return mygithub;
	}

	public void setMygithub(String mygithub) {
		this.mygithub = mygithub;
	}

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

}
