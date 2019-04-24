package com.wql.cloud.gateway.core.factory.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.wql.cloud.basic.redis.util.RedisUtil;
import com.wql.cloud.gateway.constants.GatewayConstants;
import com.wql.cloud.gateway.core.factory.ApiFactory;
import com.wql.cloud.gateway.core.model.ApiModel;
import com.wql.cloud.tool.string.JsonUtils;

/**
 * 路由工厂实现类
 */
@Component
public class ApiFactoryImpl implements ApiFactory {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * apiKey与路由映射关系集合
	 */
	private Map<String, ApiModel> apiLocalMap = new HashMap<String, ApiModel>(1000);

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 根据apiKey获取路由
	 * 
	 * @param apiKey
	 * @return
	 */
	@Override
	public ApiModel getApi(String apiKey) {
		ApiModel api = apiLocalMap.get(apiKey);
		if (null == api) {
			api = getCache(apiKey);
		}
		return api;
	}

	/**
	 * 从缓存中取出api信息并放入到内存中
	 * 
	 * @param apiKey
	 * @return
	 */
	private ApiModel getCache(String apiKey) {
		ApiModel api = new ApiModel();
		String apiKeyJson = redisUtil.getStr(GatewayConstants.SYSTEM_API + apiKey);
		if (apiKeyJson != null) {
			api = JsonUtils.fromJsonString(apiKeyJson, ApiModel.class);
			if (api != null) {
				apiLocalMap.put(apiKey, api);
			}
		}
		logger.info("从redis使用apikey获取api信息----->" + JSON.toJSONString(api));
		return api;
	}

	@Override
	public void initApiLocalMap() {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>清空apiLocalMap中的api信息>>>>>>>>>>>>>>>>>>>>>>begin>>>>>");
		apiLocalMap.clear();
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<清空apiLocalMap中的api信息<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<");
	}

}
