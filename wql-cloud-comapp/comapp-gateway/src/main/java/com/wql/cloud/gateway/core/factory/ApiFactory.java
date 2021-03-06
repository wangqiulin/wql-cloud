package com.wql.cloud.gateway.core.factory;

import com.wql.cloud.gateway.core.model.Api;

/**
 * api工厂
 */
public interface ApiFactory {

	/**
	 * 根据apiKey获取api信息
	 * 
	 * @param apiKey
	 * @return
	 */
	public Api getApi(String apiKey);

}
