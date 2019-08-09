package com.wql.cloud.systemservice.pojo.domain.merchant;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_merchant_whitelist")
public class MerchantWhitelist extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String merchantCode;

	private String whiteIp;

	private Integer whiteState;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getWhiteIp() {
		return whiteIp;
	}

	public void setWhiteIp(String whiteIp) {
		this.whiteIp = whiteIp;
	}

	public Integer getWhiteState() {
		return whiteState;
	}

	public void setWhiteState(Integer whiteState) {
		this.whiteState = whiteState;
	}

}
