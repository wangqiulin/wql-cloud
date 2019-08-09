package com.wql.cloud.systemservice.pojo.domain.api;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_gateway_api")
public class Api extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String apiGroupCode;
	
	private String apiKey;
	
	private String apiName;
	
	private Integer apiPermission;
	
	private Integer apiRouteMode;
	
	private String apiRouteServiceid;
	
	private String apiRoutePath;
	
	private Integer apiReqChecksign;
	
	private Integer apiReqDecrypt;
	
	private Integer apiResEncrypt;
	
	private Integer apiResSign;
	
	private Integer apiState;

	public String getApiGroupCode() {
		return apiGroupCode;
	}

	public void setApiGroupCode(String apiGroupCode) {
		this.apiGroupCode = apiGroupCode;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public Integer getApiPermission() {
		return apiPermission;
	}

	public void setApiPermission(Integer apiPermission) {
		this.apiPermission = apiPermission;
	}

	public Integer getApiRouteMode() {
		return apiRouteMode;
	}

	public void setApiRouteMode(Integer apiRouteMode) {
		this.apiRouteMode = apiRouteMode;
	}

	public String getApiRouteServiceid() {
		return apiRouteServiceid;
	}

	public void setApiRouteServiceid(String apiRouteServiceid) {
		this.apiRouteServiceid = apiRouteServiceid;
	}

	public String getApiRoutePath() {
		return apiRoutePath;
	}

	public void setApiRoutePath(String apiRoutePath) {
		this.apiRoutePath = apiRoutePath;
	}

	public Integer getApiReqChecksign() {
		return apiReqChecksign;
	}

	public void setApiReqChecksign(Integer apiReqChecksign) {
		this.apiReqChecksign = apiReqChecksign;
	}

	public Integer getApiReqDecrypt() {
		return apiReqDecrypt;
	}

	public void setApiReqDecrypt(Integer apiReqDecrypt) {
		this.apiReqDecrypt = apiReqDecrypt;
	}

	public Integer getApiResEncrypt() {
		return apiResEncrypt;
	}

	public void setApiResEncrypt(Integer apiResEncrypt) {
		this.apiResEncrypt = apiResEncrypt;
	}

	public Integer getApiResSign() {
		return apiResSign;
	}

	public void setApiResSign(Integer apiResSign) {
		this.apiResSign = apiResSign;
	}

	public Integer getApiState() {
		return apiState;
	}

	public void setApiState(Integer apiState) {
		this.apiState = apiState;
	}

}
