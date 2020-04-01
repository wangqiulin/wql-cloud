package com.wql.cloud.basic.datasource.response;

import java.io.Serializable;

import com.wql.cloud.basic.datasource.response.constant.ApiEnum;

/**
 * 数据响应类
 * 
 * @author wangqiulin
 *
 */
public class DataResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 响应code
	 */
	private String code;

	/**
	 * 响应描述
	 */
	private String message;

	/**
	 * 返回数据
	 */
	private T data;

	public DataResponse() {
	}

	public DataResponse(ApiEnum busEnum) {
		this.code = busEnum.getCode();
		this.message = busEnum.getMessage();
	}

	public DataResponse(ApiEnum busEnum, T data) {
		this.code = busEnum.getCode();
		this.message = busEnum.getMessage();
		this.data = data;
	}

	public DataResponse(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public DataResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getmessage() {
		return message;
	}

	public void setmessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	//==================封装公用的响应方法========================//
	
	public static <T> DataResponse<T> success() {
		return new DataResponse<>(ApiEnum.SUCCESS, null);
	}
	
	public static <T> DataResponse<T> success(T data) {
		return new DataResponse<>(ApiEnum.SUCCESS, data);
	}
	
	public static <T> DataResponse<T> success(String code, T data) {
		return new DataResponse<>(code, null, data);
	}
	
	public static <T> DataResponse<T> success(String code, String message, T data) {
		return new DataResponse<>(code, message, data);
	}
	
	
	public static <T> DataResponse<T> failure() {
		return new DataResponse<>(ApiEnum.FAILURE);
	}
	
	public static <T> DataResponse<T> failure(String message) {
		return new DataResponse<>(ApiEnum.FAILURE.getCode(), message);
	}
	
	public static <T> DataResponse<T> failure(String code, String message) {
		return new DataResponse<>(code, message);
	}
	
}
