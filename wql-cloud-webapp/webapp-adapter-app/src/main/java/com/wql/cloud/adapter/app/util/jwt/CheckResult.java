package com.wql.cloud.adapter.app.util.jwt;

import io.jsonwebtoken.Claims;

public class CheckResult {

    private Boolean success;
    
    private Claims claims;
    
    private String errCode;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    
}
