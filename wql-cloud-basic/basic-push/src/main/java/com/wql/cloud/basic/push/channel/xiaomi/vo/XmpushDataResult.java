package com.wql.cloud.basic.push.channel.xiaomi.vo;

import com.wql.cloud.basic.push.route.vo.BaseDataResult;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Result;

/**
 * 小米推送消息返回结果内部封装类
 */
public class XmpushDataResult extends BaseDataResult {

	/**
	 * [ messageId=acm50435521205284466bw errorCode=0 data={"id":"acm50435521205284466bw"} trace_id=Xcm50435521205284464QW ]
	 * @param result
	 */
	public XmpushDataResult(Result result) {
		this.data = result.getData()==null?null:result.getData().toJSONString();
		ErrorCode errorCode = result.getErrorCode();
		this.success = 0==errorCode.getValue();
		this.message = errorCode.getDescription();
	}
	
	
}
