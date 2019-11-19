package com.wql.cloud.userservice.pojo.res;

import java.io.Serializable;
import java.util.Date;

public class UserExcel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date createDate;
	
	private Date updateDate;

	private String userName;
	
	private String userPwd;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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

}
