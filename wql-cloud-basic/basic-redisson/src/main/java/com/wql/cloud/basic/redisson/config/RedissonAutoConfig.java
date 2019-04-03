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

import com.wql.cloud.basic.redisson.distributeLock.DistributedLockTemplate;
import com.wql.cloud.basic.redisson.distributeLock.SingleDistributedLockTemplate;
import com.wql.cloud.basic.redisson.lock.DistributedLocker;
import com.wql.cloud.basic.redisson.lock.RedissonDistributedLocker;
import com.wql.cloud.basic.redisson.lock.RedissonLockUtil;

@Configuration
@ConditionalOnClass(Config.class)
public class RedissonAutoConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedissonAutoConfig.class);
	
	@Autowired
	private RedissonProperty redssionProperties;

	/**
	 * 单例模式
	 * @return
	 */
	@Bean
	RedissonClient redissonSingle() {
		logger.info("【Redisson-单例模式】------>开始初始化 ");
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
		logger.info("【Redisson-单例模式】------>初始化成功");
		return client;
	}
	
	
	/*@Bean
	RedissonClient redissonCluster() {
		logger.info("【Redisson-集群模式】------>开始初始化 ");
		Config config = new Config();
		ClusterServersConfig clusterServersConfig = config.useClusterServers()
			.setScanInterval(2000)	// cluster state scan interval in milliseconds
			.addNodeAddress("127.0.0.1:7000", "127.0.0.1:7001")
			.addNodeAddress("127.0.0.1:7002");
		if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
			clusterServersConfig.setPassword(redssionProperties.getPassword());
		} 
		RedissonClient client = Redisson.create(config);
		logger.info("【Redisson-集群模式】------>初始化成功");
		return client;
	}
	
	
	@Bean
	RedissonClient redissonSentinel() {
		logger.info("【Redisson-哨兵模式】------>开始初始化 ");
		Config config = new Config();
		SentinelServersConfig sentinelServersConfig = config.useSentinelServers().setMasterName("mymaster")
				.addSentinelAddress("127.0.0.1:26389", "127.0.0.1:26379")
				.addSentinelAddress("127.0.0.1:26319");
		if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
			sentinelServersConfig.setPassword(redssionProperties.getPassword());
		} 
		RedissonClient client = Redisson.create(config);
		logger.info("【Redisson-哨兵模式】------>初始化成功");
		return client;
	}
	
	
	@Bean
	RedissonClient redissonMasterSlave() {
		logger.info("【Redisson-主从模式】------>开始初始化 ");
		Config config = new Config();
		MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers() 
				.setMasterAddress("127.0.0.1:6379") 
				.addSlaveAddress("127.0.0.1:6389", "127.0.0.1:6332", "127.0.0.1:6419") 
				.addSlaveAddress("127.0.0.1:6399");
		if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
			masterSlaveServersConfig.setPassword(redssionProperties.getPassword());
		} 
		RedissonClient client = Redisson.create(config);
		logger.info("【Redisson-主从模式】------>初始化成功");
		return client;
	}*/

	@Bean
	DistributedLocker distributedLocker(RedissonClient redissonClient) {
		DistributedLocker locker = new RedissonDistributedLocker();
		((RedissonDistributedLocker) locker).setRedissonClient(redissonClient);
		RedissonLockUtil.setLocker(locker);
		return locker;
	}
	
	
	@Bean
	DistributedLockTemplate distributedLockTemplate(RedissonClient redissonClient) {
	    return new SingleDistributedLockTemplate(redissonClient);
	}
	

}
