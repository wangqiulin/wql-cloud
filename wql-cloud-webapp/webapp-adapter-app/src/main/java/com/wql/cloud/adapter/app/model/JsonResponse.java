package com.wql.cloud.adapter.app.model;

import java.io.Serializable;

/**
 * 返回Json参数
 * 
 * @param <T>
 */
public class JsonResponse<T> implements Serializable {

    public JsonResponse() {};

    public JsonResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    };

    private static final long serialVersionUID = -1286631484522228910L;

    /**
     * 返回Code
     */
    private String code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

}
