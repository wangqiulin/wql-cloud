package com.wql.cloud.systemservice.pojo.domain.user;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_user_role_rel")
public class UserRoleRel extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userCode;

	private String roleCode;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

}
