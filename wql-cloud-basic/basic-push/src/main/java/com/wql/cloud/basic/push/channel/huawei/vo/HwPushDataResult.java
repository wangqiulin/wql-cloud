package com.wql.cloud.basic.push.channel.huawei.vo;

import com.alibaba.fastjson.JSONObject;
import com.wql.cloud.basic.push.route.vo.BaseDataResult;

/**
 * 华为结果信息
 */
public class HwPushDataResult extends BaseDataResult {

    private String requestId;

	public HwPushDataResult(String resp) {
        JSONObject jsonObject = JSONObject.parseObject(resp);
        String code = String.valueOf(jsonObject.get("code"));
        if ("80000000".equals(code)) {
            this.success = true;
        } else {
            this.success = false;
        }
        this.data = resp;
        this.message = String.valueOf(jsonObject.get("msg"));
	}

    public HwPushDataResult(boolean success, String message) {
        super(success, message);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
