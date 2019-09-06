package com.wql.cloud.systemservice.pojo.domain.resource;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_resource_detail")
public class ResourceDetail extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String resourceDetailCode;

	private String resourceDetailName;

	private String resourceCode;

	private String resourceDetailUrl;

	private Integer resourceDetailSort;

	private Integer resourceDetailState;

	public String getResourceDetailCode() {
		return resourceDetailCode;
	}

	public void setResourceDetailCode(String resourceDetailCode) {
		this.resourceDetailCode = resourceDetailCode;
	}

	public String getResourceDetailName() {
		return resourceDetailName;
	}

	public void setResourceDetailName(String resourceDetailName) {
		this.resourceDetailName = resourceDetailName;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceDetailUrl() {
		return resourceDetailUrl;
	}

	public void setResourceDetailUrl(String resourceDetailUrl) {
		this.resourceDetailUrl = resourceDetailUrl;
	}

	public Integer getResourceDetailSort() {
		return resourceDetailSort;
	}

	public void setResourceDetailSort(Integer resourceDetailSort) {
		this.resourceDetailSort = resourceDetailSort;
	}

	public Integer getResourceDetailState() {
		return resourceDetailState;
	}

	public void setResourceDetailState(Integer resourceDetailState) {
		this.resourceDetailState = resourceDetailState;
	}

}
