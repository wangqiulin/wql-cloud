package com.wql.cloud.basic.security.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 管理平台请求包装类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemRequest<T> {

    /**用户code*/
    private String userCode;

    /**用户名称*/
    private String userName;

    /**数据*/
    private T data;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
