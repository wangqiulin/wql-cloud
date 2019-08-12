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

	/**
	 * 加载黑名单列表
	 */
    @Override
	public void initBlackList() {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>加载黑名单信息>>>>>>>>>>>>>>>>>>>>>>begin>>>>>");
		// 清空 blacklist
		blackList.clear();
		// 加载blacklist
		List<Object> list = redisUtil.getList(GatewayConstants.SYSTEM_BLACK_LIST);
		if (!CollectionUtils.isEmpty(list)) {
			for (Object object : list) {
				String ip = String.valueOf(object);
				blackList.add(ip);
			}
		}
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<加载黑名单信息<<<<<<<<<<<<<<<<<<<<<<<<end<<<<<");
	}

}
