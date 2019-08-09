package com.wql.cloud.systemservice.pojo.domain.api;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_gateway_api_group")
public class ApiGroup extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String apiGroupCode;
	
	private String apiGroupName;
	
	private Integer apiGroupState;

	public String getApiGroupCode() {
		return apiGroupCode;
	}

	public void setApiGroupCode(String apiGroupCode) {
		this.apiGroupCode = apiGroupCode;
	}

	public String getApiGroupName() {
		return apiGroupName;
	}

	public void setApiGroupName(String apiGroupName) {
		this.apiGroupName = apiGroupName;
	}

	public Integer getApiGroupState() {
		return apiGroupState;
	}

	public void setApiGroupState(Integer apiGroupState) {
		this.apiGroupState = apiGroupState;
	}

}
