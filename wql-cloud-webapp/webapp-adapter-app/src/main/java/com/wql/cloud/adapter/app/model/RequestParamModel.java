package com.wql.cloud.adapter.app.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.adapter.app.form.SystemModel;

/**
 * 请求参数
 */
public class RequestParamModel implements Serializable {

	private static final long serialVersionUID = 3950534200473365363L;

	/**
	 * 商户代码
	 */
	private String merchantCode;

	/**
	 * apiKey
	 */
	private String apiKey;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 系统参数
	 */
	private SystemModel system;

	/**
	 * 数据
	 */
	private JSONObject data;

	/**
	 * 签名
	 */
	private String sign;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public SystemModel getSystem() {
		return system;
	}

	public void setSystem(SystemModel system) {
		this.system = system;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

}
