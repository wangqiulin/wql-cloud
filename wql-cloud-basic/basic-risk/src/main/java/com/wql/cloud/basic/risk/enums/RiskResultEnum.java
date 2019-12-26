package com.wql.cloud.basic.risk.enums;

/**
 * 风控结果返回体
 */
public enum RiskResultEnum implements Comparable<RiskResultEnum> {
	
	NO_CODE(0, "不需要验证码！", ""), 
	SECOND_COMMON_CODE(1, "智能无感知验证码！", "6d8806c6efd84814b87bcccb7e63d081"),
	SECOND_PUZZLE_CODE(2, "滑动拼图验证码！", "541b898d1af04505b267563ac6d18794"),
	SECOND_POINTS_CODE(3, "图中选点验证码！", "6ce4655da6884662857768350a31008d"),
	SECOND_SMS_CODE(4, "短信上行验证码", "b89bc283fb844e7baa784a2976bc0db7"), 
	BLACK_LIST_CODE(5, "手机已经加入黑名单，不要再来访问了！", ""),
	BLACK_IP_LIST_CODE(6, "ip已经加入黑名单，不要再来访问了！", ""),
	ERROR_CODE(-1, "客户端请求太快！", "");
	
	private int status;

	private String message;

	private String id;

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	private RiskResultEnum(int status, String message, String id) {
		this.status = status;
		this.message = message;
		this.id = id;
	}

	/**
	 * 获取风控结果
	 * 
	 * @param riskCodeType
	 * @return
	 */
	public static RiskResultEnum queryRiskResultByCodeType(int riskCodeType) {
		for (RiskResultEnum riskResultEnum : values()) {
			if (riskResultEnum.getStatus() == riskCodeType) {
				return riskResultEnum;
			}
		}
		return SECOND_SMS_CODE;
	}

	/**
	 * 检测这些风控是否传url回去
	 * 
	 * @param resultEnum
	 * @return
	 */
	public static Boolean checkNeedUrl(RiskResultEnum resultEnum) {
		switch (resultEnum) {
		case NO_CODE:
			return false;
		case BLACK_LIST_CODE:
			return false;
		case BLACK_IP_LIST_CODE:
			return false;
		case ERROR_CODE:
			return false;
		default:
			return true;
		}
	}

	public String getId() {
		return id;
	}

	/**
	 * 判断风控结果是否是二次验证
	 * 
	 * @param riskResultEnum
	 * @return
	 */
	public static boolean isSecondRisk(RiskResultEnum riskResultEnum) {
		if (riskResultEnum == SECOND_COMMON_CODE || riskResultEnum == SECOND_PUZZLE_CODE
				|| riskResultEnum == SECOND_POINTS_CODE || riskResultEnum == SECOND_SMS_CODE) {
			return true;
		}
		return false;
	}
}
