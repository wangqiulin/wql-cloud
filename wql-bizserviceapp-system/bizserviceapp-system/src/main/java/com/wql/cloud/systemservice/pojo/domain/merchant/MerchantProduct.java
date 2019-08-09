package com.wql.cloud.systemservice.pojo.domain.merchant;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_merchant_product")
public class MerchantProduct extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String merchantCode;

	private String productCode;

	private Integer state;

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
