package com.wql.cloud.basic.push.channel.xiaomi.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.push.channel.vivo.impl.VivoPushServiceImpl;
import com.wql.cloud.basic.push.channel.xiaomi.XmPushService;
import com.wql.cloud.basic.push.channel.xiaomi.template.XmPushTemplate;
import com.wql.cloud.basic.push.channel.xiaomi.vo.XmpushDataResult;
import com.wql.cloud.basic.push.route.vo.MessagePushConfig;
import com.wql.cloud.basic.push.util.JsonUtils;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

/**
 * 小米推送
 */
@Service
public class XmPushServiceImpl implements XmPushService {

    public static final int MAX_SIZE = 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(VivoPushServiceImpl.class);

    @Override
    public XmpushDataResult sendNcMsg(XmPushTemplate pushTemplate, String... clientIds) {
        if (clientIds == null || clientIds.length == 0) {
            throw new IllegalArgumentException("clientId不存在!");
        }
        if (clientIds.length >= MAX_SIZE) {
            throw new IllegalArgumentException("超出最大推送限制!");
        }

        try {
            Constants.useOfficial();
            Sender sender = new Sender(pushTemplate.getAppSecret());
            Message message = new Message.Builder()
                    .passThrough(0)
                    .title(pushTemplate.getTitle())
                    .description(pushTemplate.getContent())
                    .restrictedPackageName(pushTemplate.getPackageName())
                    .extra(Constants.EXTRA_PARAM_NOTIFY_EFFECT, Constants.NOTIFY_ACTIVITY)
                    .extra(Constants.EXTRA_PARAM_INTENT_URI, MessageFormat.format(pushTemplate.getIntent(),
                            encodeUt8(JsonUtils.toJsonString(pushTemplate.getParams()))
                    ))
                    .build();

            Result send;
            if (clientIds.length == 1) {
                send = sender.send(message, clientIds[0], 1);
            } else {
                send = sender.send(message, Arrays.asList(clientIds),1);
            }
            return new XmpushDataResult(send);
        } catch (Exception e) {
            LOGGER.error("小米推送失败,{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 封装实现额外信息
     *
     * @param messagePushConfig
     * @return
     */
    @Override
    public XmPushTemplate createPushTemplate(MessagePushConfig messagePushConfig) {
        XmPushTemplate xmPushTemplate = new XmPushTemplate(messagePushConfig.getPushAppId(),
                messagePushConfig.getPushAppKey(), messagePushConfig.getPushSecretKey(), messagePushConfig.getPushPackageName(),
                messagePushConfig.getIntent());
        return xmPushTemplate;
    }

    public static void main(String[] args) throws IOException, ParseException {
        Constants.useOfficial();
        Sender sender = new Sender("aDuGNA7w4+4llzkTCZaDWw==");
        String messagePayload = "xiaosxi";
        String title = "title";
        String description = "desicption";
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName("com.wisdom.lender")
                .notifyType(1)     // 使用默认提示音提示
                .build();
        Result result = sender.send(message, "Z48DfImdA+5mBi2ZCO4PsR3AzEVx79fQ5aXcConpc2R6rIrwWWIyy2dw8EadkC0Z", 3);
        System.out.println(result.toString());
    }
}
