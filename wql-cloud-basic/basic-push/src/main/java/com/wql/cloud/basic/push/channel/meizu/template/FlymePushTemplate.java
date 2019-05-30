package com.wql.cloud.basic.push.channel.meizu.template;

import java.util.Map;

import com.wql.cloud.basic.push.route.template.PushTemplate;

/**
 * 魅族push模板
 */
public class FlymePushTemplate extends PushTemplate {


    private String appId;

    private String appSecret;

    private String activity;

    public FlymePushTemplate(String appId, String appSecret, String activity) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.activity = activity;
    }

    public FlymePushTemplate(String messageId, String title, String router, String content, Map<String, String> params, String appId, String appSecret, String activity) {
        super(messageId, title, router, content, params);
        this.appId = appId;
        this.appSecret = appSecret;
        this.activity = activity;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

}
