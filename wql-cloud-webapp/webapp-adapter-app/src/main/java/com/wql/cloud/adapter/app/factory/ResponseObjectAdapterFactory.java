package com.wql.cloud.adapter.app.factory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.wql.cloud.adapter.app.service.ResponseAdapterHandler;
import com.wql.cloud.adapter.app.service.impl.DefaultResponseAdapterHandler;
import com.wql.cloud.adapter.app.util.SpringUtil;

/**
 * 响应对象适配器工厂
 */
@Component
public class ResponseObjectAdapterFactory {
	
	/**
	 * 获取响应对象适配器处理类
	 * 
	 * @param adaptorKey
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResponseAdapterHandler getObjectAdapter(String adaptorKey) {
		if (StringUtils.isBlank(adaptorKey)) {
			return null;
		}
		if (SpringUtil.containBean(adaptorKey)) {
			return (ResponseAdapterHandler) SpringUtil.getBean(adaptorKey);
		} else {
			return (ResponseAdapterHandler) SpringUtil.getBean(DefaultResponseAdapterHandler.class);
		}
	}


}
