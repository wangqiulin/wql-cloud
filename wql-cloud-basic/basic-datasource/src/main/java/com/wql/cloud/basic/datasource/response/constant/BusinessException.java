package com.wql.cloud.basic.datasource.response.constant;

/**
 * 自定义异常
 * @author wangqiulin
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 错误code
	 */
	private String code;
	
	/**
	 * 错误原因
	 */
	private String message;

	public BusinessException(BusinessEnum busEnum) {
		this.code = busEnum.getCode();
		this.message = busEnum.getMessage();
	}
	
	public BusinessException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
