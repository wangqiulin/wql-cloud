package com.wql.cloud.systemservice.pojo.domain.api;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_gateway_api_permission")
public class ApiPermission extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String apiGroupCode;
	
	private String productCode;
	
	private Integer apiPermissionState;

	public String getApiGroupCode() {
		return apiGroupCode;
	}

	public void setApiGroupCode(String apiGroupCode) {
		this.apiGroupCode = apiGroupCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getApiPermissionState() {
		return apiPermissionState;
	}

	public void setApiPermissionState(Integer apiPermissionState) {
		this.apiPermissionState = apiPermissionState;
	}

}
