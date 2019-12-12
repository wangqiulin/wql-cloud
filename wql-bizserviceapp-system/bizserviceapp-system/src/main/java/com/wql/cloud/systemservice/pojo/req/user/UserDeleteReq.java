package com.wql.cloud.systemservice.pojo.req.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户删除-请求")
public class UserDeleteReq {

	@ApiModelProperty("用户code")
	private String userCode;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
}
