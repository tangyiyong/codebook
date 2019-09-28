package cn.bowseros.vo;

import java.io.Serializable;

/**
 *
 * Created by bowser Sep 27, 2019/2:37:03 PM
 * 
 */
public class IpInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;

	private IpInfoInner data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public IpInfoInner getData() {
		return data;
	}

	public void setData(IpInfoInner data) {
		this.data = data;
	}

}

