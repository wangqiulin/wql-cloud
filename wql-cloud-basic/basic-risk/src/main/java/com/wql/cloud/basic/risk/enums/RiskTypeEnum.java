package com.wql.cloud.basic.risk.enums;

/**
 * 风控类型
 */
public enum RiskTypeEnum {

	IP(1, "IP"), MOBILE(2, "MOBILE");

	private RiskTypeEnum(int status, String message) {
		this.status = status;
		this.message = message;
	}

	private int status;
	private String message;

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public static RiskTypeEnum getEnumByStatus(int status) {
		for (RiskTypeEnum riskTypeEnum : values()) {
			if (riskTypeEnum.getStatus() == status) {
				return riskTypeEnum;
			}
		}
		return MOBILE;
	}
}
