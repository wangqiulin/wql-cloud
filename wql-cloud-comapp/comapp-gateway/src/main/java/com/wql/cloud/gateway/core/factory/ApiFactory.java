package com.wql.cloud.gateway.core.factory;

import com.wql.cloud.gateway.core.model.ApiModel;

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
	public ApiModel getApi(String apiKey);

	/**
	 * 初始化apiLocalMap
	 */
	public void initApiLocalMap();
}
