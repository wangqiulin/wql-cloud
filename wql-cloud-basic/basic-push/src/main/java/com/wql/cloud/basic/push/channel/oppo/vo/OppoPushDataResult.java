package com.wql.cloud.basic.push.channel.oppo.vo;

import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.push.route.vo.BaseDataResult;

/**
 * 魅族结果信息
 */
public class OppoPushDataResult extends BaseDataResult {

    private String requestId;

	public OppoPushDataResult(String resp) {
        JSONObject jsonObject = JSONObject.parseObject(resp);
        String code = String.valueOf(jsonObject.get("code"));
        if ("0".equals(code)) {
            this.success = true;
        } else {
            this.success = false;
        }
        this.data = resp;
        this.message = String.valueOf(jsonObject.get("message"));
	}

    public OppoPushDataResult(boolean success, String message) {
        super(success, message);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
