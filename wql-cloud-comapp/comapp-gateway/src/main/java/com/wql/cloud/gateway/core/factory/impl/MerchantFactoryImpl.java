package com.wql.cloud.gateway.core.factory.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.basic.redis.util.RedisUtil;
import com.wql.cloud.gateway.core.constant.GatewayConst;
import com.wql.cloud.gateway.core.factory.MerchantFactory;
import com.wql.cloud.gateway.core.model.MerchantCacheInfo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import cn.hutool.json.JSONUtil;

/**
 * 商户工厂实现类
 */
@Component
public class MerchantFactoryImpl implements MerchantFactory {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 商户本地内存map
	 */
	private Map<String, MerchantCacheInfo> merchantLocalMap = new HashMap<String, MerchantCacheInfo>();

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 获取商户信息 优先读取本地内存,读取不到再读取redis信息,再加载到本地内存中
	 */
	@Override
	public MerchantCacheInfo getMerchant(String merchantCode) {
		MerchantCacheInfo merchant = merchantLocalMap.get(merchantCode);
		if (merchant == null) {
			merchant = getCache(merchantCode);
		}
		return merchant;
	}

	/**
	 * 读取redis缓存信息
	 * @param merchantCode
	 * @return
	 */
	private MerchantCacheInfo getCache(String merchantCode) {
		MerchantCacheInfo merchant = new MerchantCacheInfo();
		String merchantJson = redisUtil.getStr(GatewayConst.SYSTEM_MERCHANT + merchantCode);
		if (StringUtils.isNotEmpty(merchantJson)) {
			merchant = JSONUtil.toBean(merchantJson, MerchantCacheInfo.class);
			if (merchant != null) {
				merchantLocalMap.put(merchantCode, merchant);
			}
		}
		return merchant;
	}

	
	@XxlJob("refreshMerchantJob")
    public ReturnT<String> initMerchantLocalMap(String param) throws Exception {
		merchantLocalMap.clear();
		return ReturnT.SUCCESS;
	}
	
}
