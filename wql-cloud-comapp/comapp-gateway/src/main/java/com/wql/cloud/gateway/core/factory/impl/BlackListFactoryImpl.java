package com.wql.cloud.gateway.core.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.wql.cloud.basic.redis.util.RedisUtil;
import com.wql.cloud.gateway.constants.GatewayConstants;
import com.wql.cloud.gateway.core.factory.BlackListFactory;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * 黑名单工厂实现类
 */
@Component
public class BlackListFactoryImpl implements BlackListFactory {
	
    public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 黑名单列表
	 */
	private List<String> blackList = new ArrayList<String>();

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 获取黑名单列表 本地内存取不到，读取redis缓存数据，再保存到本地内存中
	 */
	@Override
	public List<String> getBlackList() {
		return blackList;
	}

    @XxlJob("blackListRefreshJobHandler")
    public ReturnT<String> initBlackList(String param) throws Exception {
    	// 清空 blacklist
		blackList.clear();
		// 加载blacklist
		List<Object> list = redisUtil.boundListAll(GatewayConstants.SYSTEM_BLACK_LIST);
		if (!CollectionUtils.isEmpty(list)) {
			for (Object obj : list) {
				blackList.add(String.valueOf(obj));
			}
		}
		return ReturnT.SUCCESS;
	}
    
    
}
