package cn.bowseros.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import codebook.commons.DateTimeUtils;

/**
 *
 * Created by bowser Sep 7, 2019/2:17:11 PM 笔记条目
 */
@TableName("NodebookItem")
public class NodebookItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
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
	 * 共享被关闭原因
	 */
	private String sharemsg;

	/**
	 * 组id
	 */
	private String pId;
	
	/**
	 * 笔记节点类型
	 */
	private int nodetype;

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
	 * 笔记名称
	 */
	@TableField(exist = false)
	private String name;
	/**
	 * 节点是否为父节点
	 */
	@TableField(exist = false)
	private String isParent;
	/**
	 * 笔记共享日期
	 */
	@TableField(exist = false)
	private String time;

	public String getTime() {
		return DateTimeUtils.getYearMonthDay(updateTime);
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIsParent() {
		return parent;
	}
	
	public void setIsParent(String isParent) {
		this.isParent = isParent;
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

	public String getName() {
		return nodeBookName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getNodetype() {
		return nodetype;
	}

	public void setNodetype(int nodetype) {
		this.nodetype = nodetype;
	}

	public String getSharemsg() {
		return sharemsg;
	}

	public void setSharemsg(String sharemsg) {
		this.sharemsg = sharemsg;
	}
	
}
