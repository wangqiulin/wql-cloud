package com.wql.cloud.adapter.app.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.wql.cloud.adapter.app.service.RequestAdapterHandler;
import com.wql.cloud.adapter.app.service.impl.DefaultRequestAdapterHandler;
import com.wql.cloud.adapter.app.util.SpringUtil;

/**
 * 请求对象适配器工厂
 */
@Component
public class RequestObjectAdapterFactory {

	/**
	 * 获取请求对象适配器处理类
	 * 
	 * @param adaptorKey
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public RequestAdapterHandler getObjectAdapter(String adaptorKey) {
		if (StringUtils.isBlank(adaptorKey)) {
			return null;
		}
		if (SpringUtil.containBean(adaptorKey)) {
			return (RequestAdapterHandler) SpringUtil.getBean(adaptorKey);
		} else {
			return (RequestAdapterHandler) SpringUtil.getBean(DefaultRequestAdapterHandler.class);
		}
	}
}
