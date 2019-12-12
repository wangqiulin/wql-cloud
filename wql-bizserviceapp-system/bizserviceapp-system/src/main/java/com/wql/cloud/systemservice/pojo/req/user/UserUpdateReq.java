package com.wql.cloud.systemservice.pojo.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户修改-请求")
public class UserUpdateReq {

	@ApiModelProperty("用户code")
	private String userCode;
	
	@ApiModelProperty("用户名")
	private String userName;
	
	@ApiModelProperty("密码")
	private String userPwd;
	
	@ApiModelProperty("状态：0禁用，1正常")
	private Integer userState;

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

}
