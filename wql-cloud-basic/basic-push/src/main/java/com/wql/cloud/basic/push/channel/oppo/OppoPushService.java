package com.wql.cloud.basic.push.channel.oppo;

import com.wql.cloud.basic.push.channel.oppo.template.OppoPushTemplate;
import com.wql.cloud.basic.push.channel.oppo.vo.OppoAuthTokenDTO;
import com.wql.cloud.basic.push.channel.oppo.vo.OppoPushDataResult;
import com.wql.cloud.basic.push.route.PushService;

/**
 * oppo push服务
 */
public interface OppoPushService extends PushService<OppoPushDataResult, OppoPushTemplate> {

    OppoAuthTokenDTO refreshToken(OppoPushTemplate oppoPushTemplate);

}
