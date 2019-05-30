package com.wql.cloud.basic.push.route.template;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 推送基础模板
 */
public class PushTemplate {

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 标题
     */
    private String title;

    /**
     * 路由
     */
    private String router;

    /**
     * 内容
     */
    private String content;

    /**
     * 参数
     */
    private Map<String,String> params;

    public PushTemplate() {
    }

    public PushTemplate(String messageId, String title, String router, String content, Map<String, String> params) {
        this.title = title;
        this.router = router;
        this.content = content;
        this.params = params;
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        if (StringUtils.isBlank(title)) {
            return "通知";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}
