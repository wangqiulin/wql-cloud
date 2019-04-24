package com.wql.cloud.gateway.core.enums;

/**
 * api权限枚举
 */
public enum ApiPermissionEnum {

	PUBLIC(0, "公共"), LOGIN(1, "登陆"), ROLE(2, "角色"), MERCHANT(3, "商户");

	/**
	 * 枚举编号
	 */
	private Integer code;

	/**
	 * 枚举值
	 */
	private String value;

	/**
	 * 构造方法
	 * 
	 * @param code
	 * @param value
	 */
	private ApiPermissionEnum(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
