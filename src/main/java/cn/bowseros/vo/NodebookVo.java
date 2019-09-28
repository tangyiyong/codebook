package cn.bowseros.vo;

import java.io.Serializable;
import java.util.Date;

import codebook.commons.DateTimeUtils;

/**
 *
 * Created by bowser Sep 7, 2019/8:11:57 PM
 * 
 */
public class NodebookVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;

	/**
	 * 笔记名称
	 */
	private String nodeBookName;

	/**
	 * 是否共享 0是 1否
	 */
	private int share;

	/**
	 * 组id
	 */
	private String pId;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 查看当前节点是否是父节点
	 */
	private String parent;

	/**
	 * 查看当前被选中的节点的 展开 / 折叠 状态 父节点才有用
	 */
	private String open;
	
	/**
	 * 用户id
	 */
	private String userid;
	
	/**
	 * 笔记内容
	 */
	private String nodecontent;
	
	/**
	 * 笔记节点类型
	 * 标签
	 */
	private String tag;
	
	/**
	 * 笔记更新时间
	 */
	//private String time;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTime() {
		return DateTimeUtils.getYearMonthDay(updateTime);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodeBookName() {
		return nodeBookName;
	}

	public void setNodeBookName(String nodeBookName) {
		this.nodeBookName = nodeBookName;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getNodecontent() {
		return nodecontent;
	}

	public void setNodecontent(String nodecontent) {
		this.nodecontent = nodecontent;
	}
	
	
}
