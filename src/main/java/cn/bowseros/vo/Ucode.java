package cn.bowseros.vo;

import java.io.Serializable;

/**
 *
 * Created by bowser Sep 18, 2019/1:37:08 PM
 * 
 */
public class Ucode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String inu;//用户名
	private String ine;//邮箱
	public String getInu() {
		return inu;
	}
	public void setInu(String inu) {
		this.inu = inu;
	}
	public String getIne() {
		return ine;
	}
	public void setIne(String ine) {
		this.ine = ine;
	}
	
	

}
