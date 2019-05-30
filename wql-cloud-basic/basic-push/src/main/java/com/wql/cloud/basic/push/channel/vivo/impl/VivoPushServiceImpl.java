package com.wql.cloud.basic.push.channel.vivo.impl;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.push.channel.vivo.VivoPushService;
import com.wql.cloud.basic.push.channel.vivo.template.VivoPushTemplate;
import com.wql.cloud.basic.push.channel.vivo.vo.VivoAuthTokenDTO;
import com.wql.cloud.basic.push.channel.vivo.vo.VivoPushDataResult;
import com.wql.cloud.basic.push.route.vo.MessagePushConfig;
import com.wql.cloud.basic.push.util.HttpUtils;
import com.wql.cloud.basic.push.util.JsonUtils;
import com.wql.cloud.basic.push.util.Md5Util;

import net.bytebuddy.utility.RandomString;

/**
 * vivo推送
 */
@Service
public class VivoPushServiceImpl implements VivoPushService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VivoPushServiceImpl.class);

    public static final String API_HOST = "https://api-push.vivo.com.cn";

    public static final String AUTH_METHOD = "/message/auth";

    public static final String ONE_METHOD = "/message/send";

    public static final String BATCH_SAVE_METHOD = "/message/saveListPayload";

    public static final String BATCH_METHOD = "/message/pushToList";

    public static final Map<String, VivoAuthTokenDTO> TOKEN_MAP = new HashMap<>();
    public static final String TOKEN_KEY = "authToken";

    public static final int MAX_SIZE = 1000;

    @Override
    public VivoAuthTokenDTO refreshToken(VivoPushTemplate vivoPushTemplate) {

        VivoAuthTokenDTO vivoAuthTokenDTO = new VivoAuthTokenDTO();
        try {
            long currentTimeMillis = System.currentTimeMillis();

            Map<String,Object> params = new HashMap<>();
            params.put("appId", vivoPushTemplate.getAppId());
            params.put("appKey", vivoPushTemplate.getAppKey());
            params.put("timestamp", currentTimeMillis);
            params.put("sign", Md5Util.md5(vivoPushTemplate.getAppId() + vivoPushTemplate.getAppKey() + currentTimeMillis + vivoPushTemplate.getAppSecret()).toLowerCase());
            String s = HttpUtils.postHttpJson(API_HOST + AUTH_METHOD, params);

            JSONObject res = JSONObject.parseObject(s);
            if ("0".equals(String.valueOf(res.get("result")))) {
                vivoAuthTokenDTO.setAuthToken(String.valueOf(res.get("authToken")));
                vivoAuthTokenDTO.setCreateTime(currentTimeMillis);
                TOKEN_MAP.put(TOKEN_KEY + vivoPushTemplate.getAppId(), vivoAuthTokenDTO);
            }
        } catch (Exception e) {
            LOGGER.error("调用申请Token异常,{}", e);
            throw new RuntimeException(e);
        }
        return vivoAuthTokenDTO;
    }

    @Override
    public VivoPushDataResult sendNcMsg(VivoPushTemplate pushTemplate, String... clientIds) {

        if (clientIds == null || clientIds.length == 0) {
            throw new IllegalArgumentException("clientId不存在!");
        }
        if (clientIds.length >= MAX_SIZE) {
            throw new IllegalArgumentException("超出最大推送限制!");
        }

        VivoAuthTokenDTO accessTokenDTO = TOKEN_MAP.get(TOKEN_KEY + pushTemplate.getAppKey());

        //校验token有效期
        if (accessTokenDTO == null) {
            accessTokenDTO = refreshToken(pushTemplate);
        }
        long tokenExpiredTime = accessTokenDTO.getCreateTime() + 1000 * 60 * 60 * 24L;
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            accessTokenDTO = refreshToken(pushTemplate);
        }

        try {
            if (clientIds.length > 1) {
                return sendNcBatch(pushTemplate, clientIds, accessTokenDTO);
            }

            Map<String,Object> body = createBody(pushTemplate);
            body.put("regId", clientIds[0]);
//            body.put("authToken", accessTokenDTO.getAuthToken());
//            String s = HttpUtils.postHttpForm(API_HOST + ONE_METHOD, body);
            String s = HttpUtils.authedJsonPost(API_HOST + ONE_METHOD, JsonUtils.toJsonString(body), "authToken", accessTokenDTO.getAuthToken());
            return new VivoPushDataResult(s);
        } catch (Exception e) {
            LOGGER.error("vivo单推失败,{}", e);
            throw new RuntimeException(e);
        }
    }

    private VivoPushDataResult sendNcBatch(VivoPushTemplate pushTemplate, String[] clients, VivoAuthTokenDTO accessTokenDTO) {
        try {
            Map<String,Object> body = createBody(pushTemplate);
            body.put("authToken", accessTokenDTO.getAuthToken());
            String s = HttpUtils.postHttpForm(API_HOST + BATCH_SAVE_METHOD, body);
            JSONObject jsonObject = JSONObject.parseObject(s);
            if (!"0".equals(jsonObject.get("code"))) {
                throw new RuntimeException("保存群推消息公共体失败");
            }

            String taskId = String.valueOf(jsonObject.get("taskId"));

            body.clear();
            body.put("regIds", JsonUtils.toJsonString(clients));
            body.put("taskId", taskId);
            body.put("requestId", RandomString.hashOf(50));
            String res = HttpUtils.authedJsonPost(API_HOST + BATCH_METHOD, JsonUtils.toJsonString(body), "authToken", accessTokenDTO.getAuthToken());

            return new VivoPushDataResult(res);
        } catch (Exception e) {
            LOGGER.error("vivo单推失败,{}", e);
            throw new RuntimeException(e);
        }

    }

    private Map<String,Object> createBody(VivoPushTemplate pushTemplate) throws UnsupportedEncodingException {
        Map<String,Object> body = new HashMap<>();
        body.put("notifyType", 4);
        body.put("title", pushTemplate.getTitle());
        body.put("content", pushTemplate.getContent());
        body.put("skipType", 4);
        body.put("skipContent", MessageFormat.format(pushTemplate.getIntent(),
                encodeUt8(JSONObject.toJSONString(pushTemplate.getParams()))
        ));
        body.put("requestId", RandomString.hashOf(50));
        return body;
    }

    @Override
    public VivoPushTemplate createPushTemplate(MessagePushConfig messagePushConfig) {
        VivoPushTemplate vivoPushTemplate = new VivoPushTemplate(messagePushConfig.getPushAppKey(), messagePushConfig.getPushAppId(),
                messagePushConfig.getPushSecretKey(), messagePushConfig.getIntent());
        return vivoPushTemplate;
    }
}
