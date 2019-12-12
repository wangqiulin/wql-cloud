package com.wql.cloud.gateway.core.factory;

import java.util.List;

/**
 * 黑名单工厂
 */
public interface BlackListFactory {

	/**
	 * 获取黑名单列表
	 * @return
	 */
	public List<String> getBlackList();
	
}
