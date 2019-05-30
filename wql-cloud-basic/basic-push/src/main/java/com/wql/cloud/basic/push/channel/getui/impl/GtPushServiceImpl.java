package com.wql.cloud.basic.push.channel.getui.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.push.channel.getui.GtPushService;
import com.wql.cloud.basic.push.channel.getui.old.BaseMessageTemplate;
import com.wql.cloud.basic.push.channel.getui.old.GetuiMessageTemplate;
import com.wql.cloud.basic.push.channel.getui.old.GtAppService;
import com.wql.cloud.basic.push.channel.getui.template.GtPushTemplate;
import com.wql.cloud.basic.push.channel.getui.vo.GetuiDataResult;
import com.wql.cloud.basic.push.channel.vivo.impl.VivoPushServiceImpl;
import com.wql.cloud.basic.push.route.template.PushTemplate;
import com.wql.cloud.basic.push.route.vo.MessagePushConfig;
import com.wql.cloud.basic.push.util.BeanUtils;
import com.wql.cloud.basic.push.util.JsonUtils;

/**
 * TODO(需重写,暂调用原接口)
 */
@Service
public class GtPushServiceImpl implements GtPushService {

    @Autowired
    private GtAppService gtAppService;

    public static final int MAX_SIZE = 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(VivoPushServiceImpl.class);

    @Override
    public GetuiDataResult sendNcMsg(GtPushTemplate pushTemplate, String... clientIds) {
        if (clientIds == null || clientIds.length == 0) {
            throw new IllegalArgumentException("clientId不存在!");
        }
        if (clientIds.length >= MAX_SIZE) {
            throw new IllegalArgumentException("超出最大推送限制!");
        }

        try {
            gtAppService.setPushUrl(pushTemplate.getGtUrl());

            GetuiMessageTemplate template = (GetuiMessageTemplate) gtAppService
                    .createMessageTemplate(pushTemplate.getAppId(), pushTemplate.getAppKey(), pushTemplate.getAppSecret(),
                            true, 10 * 60 * 1000L, BaseMessageTemplate.PUSH_AUTO)
                    .setBasicView(pushTemplate.getTitle(), pushTemplate.getContent(), null, null);
            //设置透传
            template.setTemplateType(BaseMessageTemplate.TRANSMISSION_TEMPLATE_TYPE);
            template.setTransmissionContent(JsonUtils.toJsonString(BeanUtils.copy(pushTemplate, PushTemplate.class)));

            GetuiDataResult gtResult;
            if (clientIds.length == 1) {
                gtResult = gtAppService.pushtoSingleByClientId(template, clientIds[0]);
            } else {
                gtResult = gtAppService.pushListByClientIds(template, Arrays.asList(clientIds));
            }
            return gtResult;
        } catch (Exception e) {
            LOGGER.error("个推推送失败,{}", e);
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
    public GtPushTemplate createPushTemplate(MessagePushConfig messagePushConfig) {
        GtPushTemplate gtPushTemplate = new GtPushTemplate(messagePushConfig.getPushAppId(),
                messagePushConfig.getPushAppKey(), messagePushConfig.getPushSecretKey(), messagePushConfig.getPushGtUrl());
        return gtPushTemplate;
    }
}
