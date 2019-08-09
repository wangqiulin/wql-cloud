package com.wql.cloud.systemservice.pojo.domain.product;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_product")
public class Product extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productCode;

	private String productName;

	/**
	 * 产品类型 0内部 1外部
	 */
	private Integer productType;

	/**
	 * 状态，0 停用 1 启用
	 */
	private Integer productState;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Integer getProductState() {
		return productState;
	}

	public void setProductState(Integer productState) {
		this.productState = productState;
	}

}
