package com.wql.cloud.gateway.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * 商户信息
 */
public class MerchantCacheInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 商户号
	 */
	private String merchantCode;

	/**
	 * 白名单
	 */
	private List<String> whiteList;

	/**
	 * api权限列表
	 */
	private List<String> apiList;

	/**
	 * 商户公钥
	 */
	private String merchantPublicKey;

	/**
	 * 商户私钥
	 */
	private String merchantPrivateKey;

	/**
	 */
	private String platformPublicKey;

	/**
	 * 平台私钥
	 */
	private String platformPrivateKey;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public List<String> getWhiteList() {
		return whiteList;
	}

	public void setWhiteList(List<String> whiteList) {
		this.whiteList = whiteList;
	}

	public List<String> getApiList() {
		return apiList;
	}

	public void setApiList(List<String> apiList) {
		this.apiList = apiList;
	}

	public String getMerchantPublicKey() {
		return merchantPublicKey;
	}

	public void setMerchantPublicKey(String merchantPublicKey) {
		this.merchantPublicKey = merchantPublicKey;
	}

	public String getMerchantPrivateKey() {
		return merchantPrivateKey;
	}

	public void setMerchantPrivateKey(String merchantPrivateKey) {
		this.merchantPrivateKey = merchantPrivateKey;
	}

	public String getPlatformPublicKey() {
		return platformPublicKey;
	}

	public void setPlatformPublicKey(String platformPublicKey) {
		this.platformPublicKey = platformPublicKey;
	}

	public String getPlatformPrivateKey() {
		return platformPrivateKey;
	}

	public void setPlatformPrivateKey(String platformPrivateKey) {
		this.platformPrivateKey = platformPrivateKey;
	}
}