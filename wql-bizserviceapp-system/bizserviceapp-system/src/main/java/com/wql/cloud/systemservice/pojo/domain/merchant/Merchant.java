package com.wql.cloud.systemservice.pojo.domain.merchant;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_merchant")
public class Merchant extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String merchantCode;

	private String merchantName;

	private String platformPublicKey;

	private String platformPrivateKey;

	private String merchantPublicKey;

	private String merchantPrivateKey;

	private Integer state;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
