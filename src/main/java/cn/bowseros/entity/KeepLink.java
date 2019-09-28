package cn.bowseros.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *
 * Created by bowser Sep 10, 2019/10:21:38 PM
 * 
 */
@TableName("KeepLink")
public class KeepLink implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;
	
	/**
	 * 链接名称
	 */
	private String kname;
	
	/**
	 * 链接地址
	 */
	private String kurl;
	
	/**
	 * 用户id
	 */
	private String userid;
	
	@TableField(exist=false)
	private String url;
	@TableField(exist=false)
	private String name;
	@TableField(exist=false)
	private String target;

	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTarget() {
		return "_blank";
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUrl() {
		return kurl;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return kname;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKname() {
		return kname;
	}

	public void setKname(String kname) {
		this.kname = kname;
	}

	public String getKurl() {
		return kurl;
	}

	public void setKurl(String kurl) {
		this.kurl = kurl;
	}
	
	
}
