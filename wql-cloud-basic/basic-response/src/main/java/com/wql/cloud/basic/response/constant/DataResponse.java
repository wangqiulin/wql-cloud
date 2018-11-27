package com.wql.cloud.basic.response.constant;

/**
 * 数据响应类
 * @author wangqiulin
 *
 */
public class DataResponse {

	/**
	 * 响应code
	 */
	private String code;
	
	/**
	 * 响应描述
	 */
	private String msg;
	
	/**
	 * 返回数据
	 */
	private Object data;
	
	public DataResponse() {
	}

	public DataResponse(BusinessEnum busEnum) {
		this.code = busEnum.getCode();
		this.msg = busEnum.getMsg();
	}
	
	public DataResponse(BusinessEnum busEnum, Object data) {
		this.code = busEnum.getCode();
		this.msg = busEnum.getMsg();
		this.data = data;
	}
	
	public DataResponse(String code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	public DataResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}
