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
import com.wql.cloud.gateway.core.model.Api;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

import cn.hutool.json.JSONUtil;

/**
 * 路由工厂实现类
 */
@Component
public class ApiFactoryImpl implements ApiFactory {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * apiKey与路由映射关系集合
	 */
	private Map<String, Api> apiLocalMap = new HashMap<String, Api>(1000);

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 根据apiKey获取路由
	 * 
	 * @param apiKey
	 * @return
	 */
	@Override
	public Api getApi(String apiKey) {
		Api api = apiLocalMap.get(apiKey);
		if (null == api) {
			api = getCache(apiKey);
		}
		return api;
	}

	/**
	 * 从缓存中取出api信息并放入到内存中
	 * @param apiKey
	 * @return
	 */
	private Api getCache(String apiKey) {
		Api api = new Api();
		String apiKeyJson = redisUtil.getStr(GatewayConstants.SYSTEM_API + apiKey);
		if (apiKeyJson != null) {
			api = JSONUtil.toBean(apiKeyJson, Api.class);
			if (api != null) {
				apiLocalMap.put(apiKey, api);
			}
		}
		logger.info("从redis使用apikey获取api信息----->" + JSON.toJSONString(api));
		return api;
	}

	
	@XxlJob("apiRefreshJobHandler")
    public ReturnT<String> loadApiCache(String param) throws Exception {
		apiLocalMap.clear();
		return ReturnT.SUCCESS;
	}

}
