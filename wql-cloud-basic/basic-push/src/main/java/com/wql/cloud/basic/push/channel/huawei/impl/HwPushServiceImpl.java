package com.wql.cloud.basic.push.channel.huawei.impl;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.push.channel.huawei.HwPushService;
import com.wql.cloud.basic.push.channel.huawei.template.HwPushTemplate;
import com.wql.cloud.basic.push.channel.huawei.vo.HwAccessTokenDTO;
import com.wql.cloud.basic.push.channel.huawei.vo.HwPushDataResult;
import com.wql.cloud.basic.push.route.vo.MessagePushConfig;
import com.wql.cloud.basic.push.util.HttpUtils;

/**
 * 华为推送
 */
@Service
public class HwPushServiceImpl implements HwPushService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HwPushServiceImpl.class);

    /**
     * 已申请的token
     * key:appId+ACCESS_TOKEN_KEY
     */
    public static final Map<String, HwAccessTokenDTO> ACCESS_TOKEN_MAP = new HashMap<>();
    public static final String ACCESS_TOKEN_KEY = "accessToken";

    /**
     * 获取认证Token的URL
     */
    public static final String TOKEN_URL = "https://login.vmall.com/oauth2/token";

    /**
     * apiUrl
     */
    public static final String API_URL = "https://api.push.hicloud.com/pushsend.do";

    /**
     * 本接口固定为openpush.message.api.send。
     */
    public static final String API_SEND = "openpush.message.api.send";

    public static final int MAX_SIZE = 100;

    @Override
    public HwAccessTokenDTO refreshToken(HwPushTemplate hwPushTemplate) {
        try {
            HwAccessTokenDTO accessToken = new HwAccessTokenDTO();

            Map<String, Object> params = new HashMap<>();
            //固定值“client_credentials”
            params.put("grant_type", "client_credentials");
            //申请应用时获得的应用 ID，对应华为开发者联盟网站申请应用的时候获取的AppID。
            params.put("client_id", encodeUt8(hwPushTemplate.getAppId()));
            //申请应用时获得的应用密钥，对应华为开发者联盟网站申请应用的时候获取的AppSecret。
            params.put("client_secret", encodeUt8(hwPushTemplate.getAppSecret()));

            String resp = HttpUtils.postHttpForm(TOKEN_URL, params);
            JSONObject jsonObject = JSONObject.parseObject(resp);

            Object error = jsonObject.get("error");
            if (StringUtils.isBlank((String) error)) {
                accessToken.setAccessToken(String.valueOf(jsonObject.get("access_token")));
                accessToken.setTokenExpiredTime((Integer) jsonObject.get("expires_in") * 1000L);
                ACCESS_TOKEN_MAP.put(ACCESS_TOKEN_KEY + hwPushTemplate.getAppId(), accessToken);
            } else {
                throw new ValidationException("请求token有误,错误码" + error);
            }

            return accessToken;
        } catch (Exception e) {
            LOGGER.error("华为获取token失败{}", e);
            return null;
        }
    }


    @Override
    public HwPushDataResult sendNcMsg(HwPushTemplate hwPushTemplate, String... clientIds) {
        if (clientIds == null || clientIds.length == 0) {
            throw new IllegalArgumentException("clientId不存在!");
        }

        if (clientIds.length >= MAX_SIZE) {
            throw new IllegalArgumentException("超出最大推送限制!");
        }
        try {

            HwAccessTokenDTO accessTokenDTO = ACCESS_TOKEN_MAP.get(ACCESS_TOKEN_KEY + hwPushTemplate.getAppId());
            //校验token有效期
            if (accessTokenDTO == null || accessTokenDTO.getTokenExpiredTime() <= System.currentTimeMillis()) {
                accessTokenDTO = refreshToken(hwPushTemplate);
            }

            JSONObject payLoad = createPayLoad(hwPushTemplate);

            Map<String, Object> params = new HashMap<>();
            params.put("access_token", encodeUt8(accessTokenDTO.getAccessToken()));
            params.put("nsp_svc", encodeUt8(API_SEND));
            params.put("nsp_ts", encodeUt8(String.valueOf(System.currentTimeMillis() / 1000)));
            params.put("device_token_list", encodeUt8(JSONArray.toJSONString(clientIds)));
            params.put("payload", encodeUt8(payLoad.toJSONString()));

            JSONObject nspCtx = new JSONObject();
            nspCtx.put("ver", 1);
            nspCtx.put("appId", encodeUt8(hwPushTemplate.getAppId()));

            String resp = HttpUtils.postHttpForm(API_URL + "" +
                    "?nsp_ctx=" + encodeUt8(nspCtx.toString()), params);

            return new HwPushDataResult(resp);

        } catch (Exception e) {
            LOGGER.error("华为push失败{}", e);
            return new HwPushDataResult(false, e.getMessage());
        }
    }

    /**
     * 封装payLoad
     *
     * @param hwPushTemplate
     * @return
     */
    private JSONObject createPayLoad(HwPushTemplate hwPushTemplate) throws UnsupportedEncodingException {
        JSONObject body = new JSONObject();
        body.put("title", hwPushTemplate.getTitle());
        body.put("content", hwPushTemplate.getContent());

        JSONObject param = new JSONObject();
        //定义需要打开的appPkgName
        param.put("appPkgName", hwPushTemplate.getAppPkgName());
        param.put("intent", MessageFormat.format(hwPushTemplate.getIntent(),
                encodeUt8(JSONObject.toJSONString(hwPushTemplate.getParams()))
        ));

        JSONObject action = new JSONObject();
        //1 自定义行为：行为由参数intent定义
        action.put("type", 1);
        action.put("param", param);//消息点击动作参数

        JSONObject msg = new JSONObject();
        msg.put("type", 3);//3: 通知栏消息，异步透传消息请根据接口文档设置
        msg.put("action", action);//消息点击动作
        msg.put("body", body);//通知栏消息body内容

        JSONObject hps = new JSONObject();//华为PUSH消息总结构体
        hps.put("msg", msg);

        JSONObject payload = new JSONObject();
        payload.put("hps", hps);

        return payload;
    }

    @Override
    public HwPushTemplate createPushTemplate(MessagePushConfig messagePushConfig) {
        HwPushTemplate hwPushTemplate = new HwPushTemplate(messagePushConfig.getPushSecretKey(), messagePushConfig.getPushAppId(),
                messagePushConfig.getIntent(),messagePushConfig.getPushPackageName());
        return hwPushTemplate;
    }

}
