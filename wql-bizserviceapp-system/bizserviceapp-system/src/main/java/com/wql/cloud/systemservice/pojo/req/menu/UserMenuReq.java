package com.wql.cloud.systemservice.pojo.req.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("用户菜单-请求")
public class UserMenuReq {

	@ApiModelProperty("用户名")
	private String userName;
	
	@ApiModelProperty("应用key")
	private String webappKey;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWebappKey() {
		return webappKey;
	}

	public void setWebappKey(String webappKey) {
		this.webappKey = webappKey;
	}

}
