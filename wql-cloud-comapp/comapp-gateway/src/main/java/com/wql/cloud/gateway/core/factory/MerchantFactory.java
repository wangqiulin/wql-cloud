package com.wql.cloud.gateway.core.factory;

import com.wql.cloud.gateway.core.model.Merchant;

/**
 * 商户工厂接口
 */
public interface MerchantFactory {

	/**
	 * 获取商户信息
	 * @param merchantCode
	 * @return
	 */
	public Merchant getMerchant(String merchantCode);
	
	/**
	 * 加载商户信息
	 */
	public void initMerchantLocalMap();
}