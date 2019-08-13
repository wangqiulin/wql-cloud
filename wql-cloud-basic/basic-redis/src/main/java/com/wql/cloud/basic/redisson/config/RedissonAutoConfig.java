package com.wql.cloud.basic.redisson.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wql.cloud.basic.redisson.distributelock.DistributedLockTemplate;
import com.wql.cloud.basic.redisson.distributelock.SingleDistributedLockTemplate;

/**
 * https://blog.csdn.net/zilong_zilong/article/details/78252037
 * 
 * @author wangqiulin
 *
 */
@Configuration
//@ConditionalOnClass(Config.class)
@ConditionalOnExpression("${spring.redisson.switch:false}")
public class RedissonAutoConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedissonAutoConfig.class);
	
	@Autowired
	private RedissonProperty redissionProperty;

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
		String password = redissionProperty.getPassword();
		if (StringUtils.isNotBlank(password)) {
			serverConfig.setPassword(password);
		}
		RedissonClient client = Redisson.create(config);
		logger.info("【Redisson-单例模式】------>初始化成功");
		return client;
	}
	
	
	//主节点不能挂，由于只配置了一个master
//	@Bean
//	RedissonClient redissonSentinel() {
//		logger.info("【Redisson-哨兵模式】------>开始初始化 ");
//		Config config = new Config();
//		config.setCodec(new org.redisson.client.codec.StringCodec());
//		SentinelServersConfig sentinelServersConfig = config.useSentinelServers().setMasterName("mymaster")
//				.addSentinelAddress("redis://192.168.1.92:26379", "redis://192.168.1.91:26379")
//				.addSentinelAddress("redis://192.168.1.90:26379");
//		String password = redissionProperty.getPassword();
//		if (StringUtils.isNotBlank(password)) {
//			sentinelServersConfig.setPassword(password);
//		}
//		RedissonClient client = Redisson.create(config);
//		logger.info("【Redisson-哨兵模式】------>初始化成功");
//		return client;
//	}
	
	
	//主节点不能挂
//	@Bean
//	RedissonClient redissonMasterSlave() {
//		logger.info("【Redisson-主从模式】------>开始初始化 ");
//		Config config = new Config();
//		config.setCodec(new org.redisson.client.codec.StringCodec());
//		MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers() 
//				.setMasterAddress("redis://192.168.1.92:6379") 
//				.addSlaveAddress("redis://192.168.1.90:6379", "redis://192.168.1.91:6379");
//		String password = redissionProperty.getPassword();
//		if (StringUtils.isNotBlank(password)) {
//			masterSlaveServersConfig.setPassword(password);
//		} 
//		RedissonClient client = Redisson.create(config);
//		logger.info("【Redisson-主从模式】------>初始化成功");
//		return client;
//	}
	
	
//	@Bean
//	RedissonClient redissonCluster() {
//		logger.info("【Redisson-集群模式】------>开始初始化 ");
//		Config config = new Config();
//		config.setCodec(new org.redisson.client.codec.StringCodec());
//		ClusterServersConfig clusterServersConfig = config.useClusterServers()
//			.setScanInterval(2000)	// cluster state scan interval in milliseconds
//			.addNodeAddress("redis://127.0.0.1:7000", "redis://127.0.0.1:7001")
//			.addNodeAddress("redis://127.0.0.1:7002");
//		String password = redissionProperty.getPassword();
//		if (StringUtils.isNotBlank(password)) {
//			clusterServersConfig.setPassword(password);
//		}
//		RedissonClient client = Redisson.create(config);
//		logger.info("【Redisson-集群模式】------>初始化成功");
//		return client;
//	}
	
	@Bean
	DistributedLockTemplate distributedLockTemplate(RedissonClient redissonClient) {
	    return new SingleDistributedLockTemplate(redissonClient);
	}

}
