package com.wql.cloud.basic.push.channel.getui.template;

import java.util.Map;

import com.wql.cloud.basic.push.route.template.PushTemplate;

/**
 * 个推push模板
 */
public class GtPushTemplate extends PushTemplate {

    private String appId;

    private String appKey;

    private String appSecret;

    private String gtUrl;

    public GtPushTemplate(String appId, String appKey, String appSecret, String gtUrl) {
        this.appId = appId;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.gtUrl = gtUrl;
    }

    public GtPushTemplate(String messageId, String title, String router, String content, Map<String, String> params, String appId, String appKey, String appSecret, String gtUrl) {
        super(messageId, title, router, content, params);
        this.appId = appId;
        this.appKey = appKey;
        this.appSecret = appSecret;
        this.gtUrl = gtUrl;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getGtUrl() {
        return gtUrl;
    }

    public void setGtUrl(String gtUrl) {
        this.gtUrl = gtUrl;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
