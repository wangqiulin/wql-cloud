package com.wql.cloud.systemservice.pojo.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户登录-请求")
public class UserLoginReq {

	@ApiModelProperty("用户名")
	private String userName;
	
	@ApiModelProperty("密码")
	private String userPwd;
	
	@ApiModelProperty("应用key")
	private String webappKey;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getWebappKey() {
		return webappKey;
	}

	public void setWebappKey(String webappKey) {
		this.webappKey = webappKey;
	}

}
