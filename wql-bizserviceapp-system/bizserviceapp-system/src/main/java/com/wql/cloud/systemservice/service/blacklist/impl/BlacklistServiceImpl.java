package com.wql.cloud.systemservice.service.blacklist.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.redis.util.RedisUtil;
import com.wql.cloud.systemservice.mapper.blacklist.BlacklistMapper;
import com.wql.cloud.systemservice.pojo.domain.blacklist.Blacklist;
import com.wql.cloud.systemservice.service.blacklist.BlacklistService;
import com.wql.cloud.tool.collect.CollectionUtils;
import com.wql.cloud.tool.json.JsonUtils;

@Service
public class BlacklistServiceImpl implements BlacklistService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	public static final String SYSTEM_BLACKLIST = "system:blacklist";
	
	@Autowired
	private BlacklistMapper blacklistMapper;
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public void loadBlacklistCache() {
		//查询黑名单
		Blacklist record = new Blacklist();
		record.setDataFlag(1);
		record.setBlackState(1);
		List<Blacklist> list = blacklistMapper.select(record);
		if(!CollectionUtils.isEmpty(list)) {
			List<String> blackIps = list.stream().map(Blacklist::getBlackIp).collect(Collectors.toList());
			redisUtil.set(SYSTEM_BLACKLIST, JsonUtils.toJsonString(blackIps));
		} else {
			redisUtil.remove(SYSTEM_BLACKLIST);
		}
	}

}
