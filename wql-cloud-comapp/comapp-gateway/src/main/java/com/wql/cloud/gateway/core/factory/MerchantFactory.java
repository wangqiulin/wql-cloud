package com.wql.cloud.gateway.core.factory;

import com.wql.cloud.gateway.core.model.MerchantCacheInfo;

/**
 * 商户工厂接口
 */
public interface MerchantFactory {

	/**
	 * 获取商户信息
	 * @param merchantCode
	 * @return
	 */
	public MerchantCacheInfo getMerchant(String merchantCode);
	
}
