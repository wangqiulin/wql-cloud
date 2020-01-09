package com.wql.cloud.systemservice.pojo.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户添加-请求")
public class UserAddReq {

	@ApiModelProperty("用户名")
	private String userName;
	
	@ApiModelProperty("密码")
	private String userPwd;

	@ApiModelProperty("状态：0禁用，1正常")
	private Integer userState;

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

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public String getWebappKey() {
		return webappKey;
	}

	public void setWebappKey(String webappKey) {
		this.webappKey = webappKey;
	}

}
