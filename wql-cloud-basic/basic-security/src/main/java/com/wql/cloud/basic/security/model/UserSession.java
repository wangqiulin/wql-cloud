package com.wql.cloud.basic.security.model;

import java.io.Serializable;

public class UserSession implements Serializable {
	
	private static final long serialVersionUID = 6736767198656376784L;

	/**用户code*/
	private String userCode;

	/**用户名称*/
	private String userName;

	/**手机*/
	private String userPhone;

	/**用户状态*/
	private String userState;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}

}
