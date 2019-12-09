package com.wql.cloud.basic.security.login;

import java.io.Serializable;

/**
 * 登录接口 Model
 */
@SuppressWarnings("serial")
public class UserLoginReq implements Serializable {

	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 登录密码
	 */
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
