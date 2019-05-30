package com.wql.cloud.basic.push.channel.vivo;

import com.wql.cloud.basic.push.channel.vivo.template.VivoPushTemplate;
import com.wql.cloud.basic.push.channel.vivo.vo.VivoAuthTokenDTO;
import com.wql.cloud.basic.push.channel.vivo.vo.VivoPushDataResult;
import com.wql.cloud.basic.push.route.PushService;

public interface VivoPushService extends PushService<VivoPushDataResult, VivoPushTemplate> {

    VivoAuthTokenDTO refreshToken(VivoPushTemplate vivoPushTemplate);
}
