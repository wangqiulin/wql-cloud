package com.wql.cloud.basic.push.channel.oppo.template;

import java.util.Map;

import com.wql.cloud.basic.push.route.template.PushTemplate;

/**
 * oppo push模板
 */
public class OppoPushTemplate extends PushTemplate {

    /**
     * OPPO-OPEN 分配给应用的 AppKey，内部开
     * 放 API 是 PUSH 分配给应用的 AppKey
     */
    private String appKey;

    private String masterSecret;

    /**
     * 打开activity地址
     * 例:com.wisdom.lender.activity.PushTranslateActivity
     */
    private String clickActionActivity;

    public OppoPushTemplate(String appKey, String masterSecret, String clickActionActivity) {
        this.appKey = appKey;
        this.masterSecret = masterSecret;
        this.clickActionActivity = clickActionActivity;
    }

    public OppoPushTemplate(String messageId, String title, String router, String content, Map<String, String> params, String appKey, String masterSecret, String clickActionActivity) {
        super(messageId, title, router, content, params);
        this.appKey = appKey;
        this.masterSecret = masterSecret;
        this.clickActionActivity = clickActionActivity;
    }

    public String getClickActionActivity() {
        return clickActionActivity;
    }

    public void setClickActionActivity(String clickActionActivity) {
        this.clickActionActivity = clickActionActivity;
    }

    public String getMasterSecret() {
        return masterSecret;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
