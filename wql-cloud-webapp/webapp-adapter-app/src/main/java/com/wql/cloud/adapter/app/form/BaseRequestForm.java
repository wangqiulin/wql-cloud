package com.wql.cloud.adapter.app.form;

import java.io.Serializable;

/**
 * 通用请求参数封装对象
 */
public class BaseRequestForm implements Serializable {

	private static final long serialVersionUID = -7933839153064689368L;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 请求方法
	 */
	private String apiKey;
	
	/**
	 * 商户code
	 */
	private String merchantCode;

	/**
	 * 系统节
	 */
	private SystemModel system;

	/**
	 * session节
	 */
	private SessionModel session;

	/**
	 * 用户输入数据
	 */
	private String data;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public SystemModel getSystem() {
		return system;
	}

	public void setSystem(SystemModel system) {
		this.system = system;
	}

	public SessionModel getSession() {
		return session;
	}

	public void setSession(SessionModel session) {
		this.session = session;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
