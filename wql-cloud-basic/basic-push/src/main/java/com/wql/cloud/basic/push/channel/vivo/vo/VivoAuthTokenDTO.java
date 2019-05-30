package com.wql.cloud.basic.push.channel.vivo.vo;

/**
 * vivo请求token
 */
public class VivoAuthTokenDTO {

    private String authToken;

    private long createTime;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
