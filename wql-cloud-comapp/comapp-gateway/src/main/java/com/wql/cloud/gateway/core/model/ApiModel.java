package com.wql.cloud.gateway.core.model;

import java.io.Serializable;

public class ApiModel implements Serializable {

	private static final long serialVersionUID = 1572655864586427854L;

	/**
	 * apiKey
	 */
	private String apiKey;

	/**
	 * apiName
	 */
	private String apiName;

	/**
	 * 路由方式 0 ribbon 1 http
	 */
	private Integer routeMode;

	/**
	 * 服务ID(ribbon时使用)
	 */
	private String routeServiceId;

	/**
	 * 请求路径
	 */
	private String routePath;

	/**
	 * api权限 0公共 1登陆 2角色 3商户
	 */
	private Integer apiPermission;

	/**
	 * 请求信息验签 1:验签 0:不验签
	 */
	private boolean apiReqChecksign;

	/**
	 * 请求信息解密 1:解密 0:不解密
	 */
	private boolean apiReqDecrypt;

	/**
	 * 返回信息加密 1:加密 0:不加密
	 */
	private boolean apiResEncrypt;

	/**
	 * 返回信息签名 1:签名 0:不签名
	 */
	private boolean apiResSign;

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

	public Integer getRouteMode() {
		return routeMode;
	}

	public void setRouteMode(Integer routeMode) {
		this.routeMode = routeMode;
	}

	public String getRouteServiceId() {
		return routeServiceId;
	}

	public void setRouteServiceId(String routeServiceId) {
		this.routeServiceId = routeServiceId;
	}

	public String getRoutePath() {
		return routePath;
	}

	public void setRoutePath(String routePath) {
		this.routePath = routePath;
	}

	public Integer getApiPermission() {
		return apiPermission;
	}

	public void setApiPermission(Integer apiPermission) {
		this.apiPermission = apiPermission;
	}

	public boolean isApiReqChecksign() {
		return apiReqChecksign;
	}

	public void setApiReqChecksign(boolean apiReqChecksign) {
		this.apiReqChecksign = apiReqChecksign;
	}

	public boolean isApiReqDecrypt() {
		return apiReqDecrypt;
	}

	public void setApiReqDecrypt(boolean apiReqDecrypt) {
		this.apiReqDecrypt = apiReqDecrypt;
	}

	public boolean isApiResEncrypt() {
		return apiResEncrypt;
	}

	public void setApiResEncrypt(boolean apiResEncrypt) {
		this.apiResEncrypt = apiResEncrypt;
	}

	public boolean isApiResSign() {
		return apiResSign;
	}

	public void setApiResSign(boolean apiResSign) {
		this.apiResSign = apiResSign;
	}

}
