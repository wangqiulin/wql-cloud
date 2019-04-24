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

/**
 * https://blog.csdn.net/zilong_zilong/article/details/78252037
 * 
 * @author wangqiulin
 *
 */
@Configuration
@ConditionalOnClass(Config.class)
public class RedissonAutoConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedissonAutoConfig.class);
	
	@Autowired
	private RedissonProperty redissionProperty;

	/**
	 * 单例模式
	 * @return
	 */
	@Bean
	RedissonClient redissonSingle() {
		logger.info("【Redisson-单例模式】------>开始初始化 ");
		Config config = new Config();
		//指定编码，默认编码为org.redisson.codec.JsonJacksonCodec
		//之前使用的spring-data-redis，用的客户端jedis，编码为org.springframework.data.redis.serializer.StringRedisSerializer
		//改用redisson后为了之间数据能兼容，这里修改编码为org.redisson.client.codec.StringCodec
		config.setCodec(new org.redisson.client.codec.StringCodec());
		SingleServerConfig serverConfig = config.useSingleServer()
				.setAddress(redissionProperty.getAddress())
				.setDatabase(redissionProperty.getDatabase())
				.setTimeout(redissionProperty.getTimeout())
				.setConnectionPoolSize(redissionProperty.getConnectionPoolSize())
				.setConnectionMinimumIdleSize(redissionProperty.getConnectionMinimumIdleSize());
		if (StringUtils.isNotBlank(redissionProperty.getPassword())) {
			serverConfig.setPassword(redissionProperty.getPassword());
		} 
		RedissonClient client = Redisson.create(config);
		logger.info("【Redisson-单例模式】------>初始化成功");
		return client;
	}
	
	
	/*@Bean
	RedissonClient redissonSentinel() {
		logger.info("【Redisson-哨兵模式】------>开始初始化 ");
		Config config = new Config();
		config.setCodec(new org.redisson.client.codec.StringCodec());
		SentinelServersConfig sentinelServersConfig = config.useSentinelServers().setMasterName("mymaster")
				.addSentinelAddress("redis://127.0.0.1:26378", "redis://127.0.0.1:26379")
				.addSentinelAddress("redis://127.0.0.1:26380");
		if (StringUtils.isNotBlank(redissionProperty.getPassword())) {
			sentinelServersConfig.setPassword(redissionProperty.getPassword());
		} 
		RedissonClient client = Redisson.create(config);
		logger.info("【Redisson-哨兵模式】------>初始化成功");
		return client;
	}*/
	
	
	/*@Bean
	RedissonClient redissonCluster() {
		logger.info("【Redisson-集群模式】------>开始初始化 ");
		Config config = new Config();
		config.setCodec(new org.redisson.client.codec.StringCodec());
		ClusterServersConfig clusterServersConfig = config.useClusterServers()
			.setScanInterval(2000)	// cluster state scan interval in milliseconds
			.addNodeAddress("127.0.0.1:7000", "127.0.0.1:7001")
			.addNodeAddress("127.0.0.1:7002");
		if (StringUtils.isNotBlank(redissionProperty.getPassword())) {
			clusterServersConfig.setPassword(redissionProperty.getPassword());
		} 
		RedissonClient client = Redisson.create(config);
		logger.info("【Redisson-集群模式】------>初始化成功");
		return client;
	}*/
	
	
	/*@Bean
	RedissonClient redissonMasterSlave() {
		logger.info("【Redisson-主从模式】------>开始初始化 ");
		Config config = new Config();
		config.setCodec(new org.redisson.client.codec.StringCodec());
		MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers() 
				.setMasterAddress("127.0.0.1:6379") 
				.addSlaveAddress("127.0.0.1:6389", "127.0.0.1:6332", "127.0.0.1:6419") 
				.addSlaveAddress("127.0.0.1:6399");
		if (StringUtils.isNotBlank(redissionProperty.getPassword())) {
			masterSlaveServersConfig.setPassword(redissionProperty.getPassword());
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
