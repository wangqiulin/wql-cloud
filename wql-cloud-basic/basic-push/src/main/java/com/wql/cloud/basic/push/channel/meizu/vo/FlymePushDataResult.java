package com.wql.cloud.basic.push.channel.meizu.vo;

import com.alibaba.fastjson.JSONObject;
import com.meizu.push.sdk.server.constant.ResultPack;
import com.meizu.push.sdk.server.model.push.PushResult;
import com.wql.cloud.basic.push.route.vo.BaseDataResult;

/**
 * 魅族结果信息
 */
public class FlymePushDataResult extends BaseDataResult {

    private String msgId;

	public FlymePushDataResult(ResultPack<PushResult> result) {
        if (result.isSucceed()) {
            this.success = true;
        } else {
            this.success = false;
        }
        this.data = JSONObject.toJSONString(result);
        this.message = result.toString();
	}

    public FlymePushDataResult(boolean success, String message) {
        super(success, message);
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
