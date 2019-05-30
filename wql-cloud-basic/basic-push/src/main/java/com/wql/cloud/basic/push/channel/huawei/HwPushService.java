package com.wql.cloud.basic.push.channel.huawei;

import com.wql.cloud.basic.push.channel.huawei.template.HwPushTemplate;
import com.wql.cloud.basic.push.channel.huawei.vo.HwAccessTokenDTO;
import com.wql.cloud.basic.push.channel.huawei.vo.HwPushDataResult;
import com.wql.cloud.basic.push.route.PushService;

/**
 * 华为push服务
 */
public interface HwPushService extends PushService<HwPushDataResult, HwPushTemplate> {

    HwAccessTokenDTO refreshToken(HwPushTemplate hwPushTemplate);

//    HwPushDataResult sendNcMsg(HwPushTemplate hwPushTemplate, String... clientIds);
}
