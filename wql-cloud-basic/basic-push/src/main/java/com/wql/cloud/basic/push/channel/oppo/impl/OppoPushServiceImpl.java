package com.wql.cloud.basic.push.channel.oppo.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.push.channel.oppo.OppoPushService;
import com.wql.cloud.basic.push.channel.oppo.template.OppoPushTemplate;
import com.wql.cloud.basic.push.channel.oppo.vo.OppoAuthTokenDTO;
import com.wql.cloud.basic.push.channel.oppo.vo.OppoPushDataResult;
import com.wql.cloud.basic.push.route.vo.MessagePushConfig;
import com.wql.cloud.basic.push.util.HttpUtils;
import com.wql.cloud.basic.push.util.WMap;

/**
 * oppo推送
 */
@Service
public class OppoPushServiceImpl implements OppoPushService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OppoPushServiceImpl.class);

    public static final String API_HOST = "https://api.push.oppomobile.com";

    public static final String AUTH_METHOD = "/server/v1/auth";

    public static final String ONE_METHOD = "/server/v1/message/notification/unicast";

    public static final String BATCH_METHOD = "/server/v1/message/notification/unicast_batch";

    public static final Map<String, OppoAuthTokenDTO> TOKEN_MAP = new HashMap<>();
    public static final String TOKEN_KEY = "authToken";

    public static final int MAX_SIZE = 1000;

    @Override
    public OppoAuthTokenDTO refreshToken(OppoPushTemplate oppoPushTemplate) {
        OppoAuthTokenDTO authTokenDTO = new OppoAuthTokenDTO();

        Map<String, Object> map = new HashMap<>();
        long currentTimeMillis = System.currentTimeMillis();
        map.put("app_key", oppoPushTemplate.getAppKey());
        map.put("sign", String2SHA256StrJava(oppoPushTemplate.getAppKey() + currentTimeMillis + oppoPushTemplate.getMasterSecret()));
        map.put("timestamp", currentTimeMillis);

        try {
            String s = HttpUtils.postHttpForm(API_HOST + AUTH_METHOD, map);
            JSONObject res = JSONObject.parseObject(s);
            if ("0".equals(String.valueOf(res.get("code")))){
                Map<String, Object> data = (Map<String, Object>) res.get("data");
                if (data == null) {
                    throw new RuntimeException("数据异常!");
                }
                authTokenDTO.setAuthToken(String.valueOf(data.get("auth_token")));
                authTokenDTO.setCreateTime(Long.parseLong(String.valueOf(data.get("create_time"))));
                TOKEN_MAP.put(TOKEN_KEY + oppoPushTemplate.getAppKey(), authTokenDTO);
            }
        } catch (Exception e) {
            LOGGER.error("调用申请Token异常,{}", e);
            throw new RuntimeException(e);
        }
        return authTokenDTO;
    }

    @Override
    public OppoPushDataResult sendNcMsg(OppoPushTemplate pushTemplate, String... clientIds) {
        if (clientIds == null || clientIds.length == 0) {
            throw new IllegalArgumentException("clientId不存在!");
        }
        if (clientIds.length >= MAX_SIZE) {
            throw new IllegalArgumentException("超出最大推送限制!");
        }

        try {
            OppoAuthTokenDTO accessTokenDTO = TOKEN_MAP.get(TOKEN_KEY + pushTemplate.getAppKey());
            //校验token有效期
            if (accessTokenDTO == null) {
                accessTokenDTO = refreshToken(pushTemplate);
            }
            long tokenExpiredTime = accessTokenDTO.getCreateTime() + 1000 * 60 * 60 * 24L;
            if (tokenExpiredTime <= System.currentTimeMillis()) {
                accessTokenDTO = refreshToken(pushTemplate);
            }

            String res;
            Map<String, Object> map = new HashMap<>();
            map.put("auth_token", accessTokenDTO.getAuthToken());
            if (clientIds.length == 1) {
                JSONObject message = createMessage(pushTemplate, clientIds[0]);
                map.put("message", encodeUt8(message.toJSONString()));
                res = HttpUtils.postHttpForm(API_HOST + ONE_METHOD, map);
            } else {
                JSONArray jsonArray = new JSONArray();
                for (String clientId : clientIds) {
                    jsonArray.add(createMessage(pushTemplate, clientId));
                }
                map.put("messages", jsonArray.toJSONString());
                res = HttpUtils.postHttpForm(API_HOST + BATCH_METHOD, map);
            }
            return new OppoPushDataResult(res);
        } catch (Exception e) {
            LOGGER.error("oppo推送异常:{}", e);
            return new OppoPushDataResult(false, e.getMessage());
        }
    }

    private JSONObject createMessage(OppoPushTemplate pushTemplate, String clientId) {
        JSONObject message = new JSONObject();
        message.put("target_type", 2);
        message.put("target_value", clientId);
        JSONObject notification = new JSONObject();
        notification.put("app_message_id", RandomUtils.nextLong());
        notification.put("title", pushTemplate.getTitle());
        notification.put("content", pushTemplate.getContent());
        notification.put("click_action_type", 4);
        notification.put("click_action_activity", pushTemplate.getClickActionActivity());
        notification.put("action_parameters", WMap.init("pushMessage", JSONObject.toJSONString(pushTemplate.getParams())));
        message.put("notification", notification);
        return message;
    }

    /**
     * 利用java原生的摘要实现SHA256加密
     *
     * @param str 加密后的报文
     * @return
     */
    private String String2SHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("{}", e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("{}", e);
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(0xFF & bytes[i]);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append('0');
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    @Override
    public OppoPushTemplate createPushTemplate(MessagePushConfig messagePushConfig) {
        OppoPushTemplate oppoPushTemplate = new OppoPushTemplate(messagePushConfig.getPushAppKey(), messagePushConfig.getPushSecretKey(),
                messagePushConfig.getActivity());
        return oppoPushTemplate;
    }

    public static void main(String[] args) {
        OppoPushTemplate oppoPushTemplate = new OppoPushTemplate("dsfsd", "title", "ceshi",
                "neirong", new HashMap<String, String>(), "b5db4a5c99c644889163cc737efc17a5", "956835799daa47589f9a18a49839c258", "com.wisdom.lender.activity.PushTranslateActivity");

        OppoPushServiceImpl oi = new OppoPushServiceImpl();
        oi.sendNcMsg(oppoPushTemplate, "CN_2aaf42f4b5052449e04d6a01de2e12e5", "CN_ecaa2f2ef518210acb607ba71dbb1d06");
    }
}
