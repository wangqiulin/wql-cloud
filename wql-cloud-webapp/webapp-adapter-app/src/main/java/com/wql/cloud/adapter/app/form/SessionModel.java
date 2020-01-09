package com.wql.cloud.adapter.app.form;

import java.io.Serializable;

/**
 * 请求入参-Session节
 */
public class SessionModel implements Serializable {
   
    private static final long serialVersionUID = 1357059570904783935L;

    /**
     * C端用户ID
     */
    private String userId;

    /**
     */
    private String token;

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

}
