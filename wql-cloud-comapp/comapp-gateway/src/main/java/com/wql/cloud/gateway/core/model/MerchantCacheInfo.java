package com.wql.cloud.gateway.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * 商户信息
 */
public class MerchantCacheInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**商户号*/
	private String mertId;

	/**白名单*/
	private List<String> whiteList;

	/**api权限列表*/
	private List<String> apiList;

	/**商户公钥*/
	private String mertPublicKey;

	/**商户私钥*/
	private String mertPrivateKey;

	/**平台公钥*/
	private String platformPublicKey;

	/**平台私钥*/
	private String platformPrivateKey;

	public String getMertId() {
		return mertId;
	}

	public void setMertId(String mertId) {
		this.mertId = mertId;
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

	public String getMertPublicKey() {
		return mertPublicKey;
	}

	public void setMertPublicKey(String mertPublicKey) {
		this.mertPublicKey = mertPublicKey;
	}

	public String getMertPrivateKey() {
		return mertPrivateKey;
	}

	public void setMertPrivateKey(String mertPrivateKey) {
		this.mertPrivateKey = mertPrivateKey;
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
