package com.wql.cloud.basic.redisson.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wql.cloud.basic.redisson.lock.DistributedLocker;
import com.wql.cloud.basic.redisson.lock.RedissonDistributedLocker;
import com.wql.cloud.basic.redisson.lock.RedissonLockUtil;

@Configuration
@ConditionalOnClass(Config.class)
public class RedissonAutoConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedissonAutoConfig.class);
	
	@Autowired
	private RedissonProperties redssionProperties;

	@Bean
	RedissonClient redissonSingle() {
		logger.info("【Redisson】------>开始初始化 ");
		Config config = new Config();
		SingleServerConfig serverConfig = config.useSingleServer()
				.setAddress(redssionProperties.getAddress())
				.setDatabase(redssionProperties.getDatabase())
				.setTimeout(redssionProperties.getTimeout())
				.setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
				.setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());
		if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
			serverConfig.setPassword(redssionProperties.getPassword());
		} 
		RedissonClient client = Redisson.create(config);
		logger.info("【Redisson】------>初始化成功");
		return client;
	}

	@Bean
	DistributedLocker distributedLocker(RedissonClient redissonClient) {
		DistributedLocker locker = new RedissonDistributedLocker();
		((RedissonDistributedLocker) locker).setRedissonClient(redissonClient);
		RedissonLockUtil.setLocker(locker);
		return locker;
	}

}
