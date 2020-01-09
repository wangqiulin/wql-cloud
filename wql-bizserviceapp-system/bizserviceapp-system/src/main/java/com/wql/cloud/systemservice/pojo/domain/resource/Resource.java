package com.wql.cloud.systemservice.pojo.domain.resource;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_resource")
public class Resource extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String resourceCode;
	
	private String resourceName;

	private String path;

	private String component;

	private Integer resourceState;

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Integer getResourceState() {
		return resourceState;
	}

	public void setResourceState(Integer resourceState) {
		this.resourceState = resourceState;
	}

}
