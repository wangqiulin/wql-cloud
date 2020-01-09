package com.wql.cloud.adapter.app.model;

import java.io.Serializable;

/**
 * 向Zuul请求参数
 */
public class RequestModel implements Serializable {

    private static final long serialVersionUID = -6573817692013310257L;

    /**
     * 请求方法Key
     */
    private String apiKey;

    /**
     * 请求参数
     */
    private RequestParamModel param;

    /**
     * @return the apiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey the apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return the param
     */
    public RequestParamModel getParam() {
        return param;
    }

    /**
     * @param param the param to set
     */
    public void setParam(RequestParamModel param) {
        this.param = param;
    }

}
