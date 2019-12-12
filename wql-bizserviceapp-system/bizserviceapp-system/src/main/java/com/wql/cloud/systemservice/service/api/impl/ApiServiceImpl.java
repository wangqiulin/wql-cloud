package com.wql.cloud.systemservice.service.api.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.redis.util.RedisUtil;
import com.wql.cloud.systemservice.mapper.api.ApiMapper;
import com.wql.cloud.systemservice.pojo.domain.api.Api;
import com.wql.cloud.systemservice.service.api.ApiService;
import com.wql.cloud.tool.json.JsonUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

@Service
public class ApiServiceImpl implements ApiService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	public static final String SYSTEM_API = "system:api:";
	
	@Autowired
	private ApiMapper apiMapper;
	@Autowired
	private RedisUtil redisUtil;
	
	@XxlJob("apiCacheJobHandler")
    public ReturnT<String> loadApiCache(String param) throws Exception {
		List<Api> apiList = apiMapper.selectAll();
		for (Api api : apiList) {
			if(api.getDataFlag() == 0 || api.getApiState() == 0) {
				redisUtil.remove(SYSTEM_API + api.getApiKey());
			} else {
				redisUtil.set(SYSTEM_API + api.getApiKey(), JsonUtils.toJsonString(api));
			}
		}
        return ReturnT.SUCCESS;
	}
	

}
