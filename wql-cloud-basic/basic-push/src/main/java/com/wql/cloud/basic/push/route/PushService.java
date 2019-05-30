package com.wql.cloud.basic.push.route;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.wql.cloud.basic.push.route.template.PushTemplate;
import com.wql.cloud.basic.push.route.vo.BaseDataResult;
import com.wql.cloud.basic.push.route.vo.MessagePushConfig;

public interface PushService<T extends BaseDataResult, R extends PushTemplate> {

	/**
	 * 发送消息
	 * 
	 * @param pushTemplate
	 * @param clientIds
	 * @return
	 */
	T sendNcMsg(R pushTemplate, String... clientIds);

	/**
	 * 封装实现额外信息
	 * 
	 * @param messagePushConfig
	 * @return
	 */
	R createPushTemplate(MessagePushConfig messagePushConfig);

	default String encodeUt8(String str) throws UnsupportedEncodingException {
		return URLEncoder.encode(str, "UTF-8");
	}

}
