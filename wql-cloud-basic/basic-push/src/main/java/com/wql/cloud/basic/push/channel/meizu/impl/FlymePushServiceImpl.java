package com.wql.cloud.basic.push.channel.meizu.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.meizu.push.sdk.server.IFlymePush;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.meizu.push.sdk.server.model.push.VarnishedMessage;
import com.wql.cloud.basic.push.channel.meizu.FlymePushService;
import com.wql.cloud.basic.push.channel.meizu.template.FlymePushTemplate;
import com.wql.cloud.basic.push.channel.meizu.vo.FlymePushDataResult;
import com.wql.cloud.basic.push.route.vo.MessagePushConfig;
import com.wql.cloud.basic.push.util.JsonUtils;
import com.wql.cloud.basic.push.util.Md5Util;

/**
 * 魅族推送
 */
@Service
public class FlymePushServiceImpl implements FlymePushService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlymePushServiceImpl.class);

    public static final String NC_API = "/garcia/api/server/push/varnished/pushByPushId";

    public static final String API_HOST = "http://server-api-push.meizu.com";

    public static final int MAX_SIZE = 1000;

    @Override
    public FlymePushDataResult sendNcMsg(FlymePushTemplate pushTemplate, String... clientIds) {
        if (clientIds == null || clientIds.length == 0) {
            throw new IllegalArgumentException("clientId不存在!");
        }

        if (clientIds.length >= MAX_SIZE) {
            throw new IllegalArgumentException("超出最大推送限制!");
        }
        try {

            IFlymePush flymePush = new IFlymePush(pushTemplate.getAppSecret());
            VarnishedMessage message = createMessage(pushTemplate);

            ResultPack<PushResult> resultPack = flymePush.pushMessage(message, Arrays.asList(clientIds));

            /*Map<String, Object> map = new HashMap<>();
            map.put("appId", pushTemplate.getAppId());
            map.put("pushIds", clientIds[0]);
            JSONObject messageJson = createPushInfo(pushTemplate);
            map.put("messageJson", messageJson.toJSONString());
            String sign = encodeUt8(getSignature(map, pushTemplate.getAppSecret())).toLowerCase();

            map.put("appId", encodeUt8(pushTemplate.getAppId()));
            map.put("pushIds", encodeUt8(clientIds[0]));
            map.put("sign", sign);
            map.put("messageJson", encodeUt8(messageJson.toJSONString()));
            String json = HttpUtils.postHttpForm(API_HOST + NC_API, map);*/

            return new FlymePushDataResult(resultPack);
        } catch (Exception e) {
            LOGGER.error("Flyme推送失败:{}", e);
            return new FlymePushDataResult(false, e.getMessage());
        }
    }

    private VarnishedMessage createMessage(FlymePushTemplate pushTemplate) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pushMessage", pushTemplate.getParams());

        VarnishedMessage message = new VarnishedMessage.Builder()
                .title(pushTemplate.getTitle())
                .content(pushTemplate.getContent())
                .clickType(1)
                .parameters(jsonObject)
                .activity(pushTemplate.getActivity())
                .build();
        message.setAppId(Long.valueOf(pushTemplate.getAppId()));
        return message;
    }

    private JSONObject createPushInfo(FlymePushTemplate pushTemplate) {
        JSONObject messageJson = new JSONObject();

        JSONObject noticeBarInfo = new JSONObject();
        noticeBarInfo.put("title", pushTemplate.getTitle());
        noticeBarInfo.put("content", pushTemplate.getContent());
        messageJson.put("noticeBarInfo", noticeBarInfo);


        JSONObject clickTypeInfo = new JSONObject();
        clickTypeInfo.put("clickType", 1);
        clickTypeInfo.put("activity", pushTemplate.getActivity());
        clickTypeInfo.put("parameters", JsonUtils.toJsonString(pushTemplate.getParams()));
        messageJson.put("clickTypeInfo", clickTypeInfo);
        return messageJson;
    }

    /**
     * @param paramMap 请求参数
     * @param secret   密钥
     * @return md5摘要
     */
    private String getSignature(Map<String, Object> paramMap, String
            secret) {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, Object> sortedParams = new TreeMap<>
                (paramMap);
        Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Map.Entry<String, Object> param : entrys) {
            basestring.append(param.getKey()).append("=").append(param.getValue());
        }
        basestring.append(secret);
        LOGGER.debug("basestring is:{}", new Object[]
                {basestring.toString()});
        // 使用MD5对待签名串求签
        return Md5Util.md5(basestring.toString());
    }

    @Override
    public FlymePushTemplate createPushTemplate(MessagePushConfig messagePushConfig) {
        FlymePushTemplate flymePushTemplate = new FlymePushTemplate(messagePushConfig.getPushAppId(),
                messagePushConfig.getPushSecretKey(), messagePushConfig.getActivity());
        return flymePushTemplate;
    }
}
