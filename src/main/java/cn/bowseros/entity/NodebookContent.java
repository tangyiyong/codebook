package cn.bowseros.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *
 * Created by bowser Sep 7, 2019/2:30:52 PM
 * 
 */
@TableName("NodebookContent")
public class NodebookContent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@TableId
	private String nid;
	
	/**
	 * 笔记内容
	 */
	private String nodecontent;
	
	
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getNodecontent() {
		return nodecontent;
	}

	public void setNodecontent(String nodecontent) {
		this.nodecontent = nodecontent;
	}
	
	
}
