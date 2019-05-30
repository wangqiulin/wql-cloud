package com.wql.cloud.basic.push.channel.vivo.vo;

import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.push.route.vo.BaseDataResult;

/**
 * vivo结果信息
 */
public class VivoPushDataResult extends BaseDataResult {

    private String requestId;

	public VivoPushDataResult(String resp) {
        JSONObject jsonObject = JSONObject.parseObject(resp);
        String code = String.valueOf(jsonObject.get("result"));
        if ("0".equals(code)) {
            this.success = true;
        } else {
            this.success = false;
        }
        this.data = resp;
        this.message = String.valueOf(jsonObject.get("desc"));
	}

    public VivoPushDataResult(boolean success, String message) {
        super(success, message);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
