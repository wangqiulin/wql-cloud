package com.wql.cloud.basic.push.channel.huawei.vo;

/**
 * 华为accessToken
 */
public class HwAccessTokenDTO {

    /**
     * 下发通知消息的认证Token
     */
    private String accessToken;


    /**
     * accessToken的过期时间
     */
    private long tokenExpiredTime;

    /**
     * Token所属应用
     */
    private Integer businessKey;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getTokenExpiredTime() {
        return tokenExpiredTime;
    }

    public void setTokenExpiredTime(long tokenExpiredTime) {
        this.tokenExpiredTime = tokenExpiredTime;
    }

    public Integer getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(Integer businessKey) {
        this.businessKey = businessKey;
    }
}
