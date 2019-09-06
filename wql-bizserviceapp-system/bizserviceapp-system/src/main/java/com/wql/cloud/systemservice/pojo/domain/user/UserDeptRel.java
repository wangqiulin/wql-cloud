package com.wql.cloud.systemservice.pojo.domain.user;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_user_dept_rel")
public class UserDeptRel extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userCode;

	private String deptCode;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

}
