package com.wql.cloud.adapter.app.model;

import java.io.Serializable;

public class ApiModel implements Serializable {

	private static final long serialVersionUID = 3919847739796280837L;

	public ApiModel() {
	};

	public ApiModel(String apiPermission) {
		this.apiPermission = apiPermission;
	};

	/**
	 * 是否必须Login
	 */
	private String apiPermission;

	public String getApiPermission() {
		return apiPermission;
	}

	public void setApiPermission(String apiPermission) {
		this.apiPermission = apiPermission;
	}

}
