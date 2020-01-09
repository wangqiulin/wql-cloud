package com.wql.cloud.systemservice.pojo.domain.role;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_role_resource_rel")
public class RoleResourceRel extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String roleCode;
	
	private String resourceCode;
	
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

}
