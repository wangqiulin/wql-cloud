package com.wql.cloud.basic.redis.config;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.wql.cloud.basic.redis.util.RedisUtil;

/**
 *
 * @author wangqiulin
 * @date 2018年5月10日
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	/**
	 * 自定义默认缓存key生成策略: 类名+方法名+参数名
	 */
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append("cache_data:");
				sb.append(target.getClass().getName()).append(".");
				sb.append(method.getName());
				/*
				 * if(params == null || params.length == 0) { sb.append(method.getName()); }
				 * else { sb.append(method.getName()).append("."); for (Object obj : params) {
				 * sb.append(obj.toString()); } }
				 */
				return sb.toString();
			}
		};
	}

	/**
	 * 设置RedisCacheManager 使用cache注解管理redis缓存
	 *
	 * @return
	 */
	@Bean
	@SuppressWarnings("rawtypes")
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		// 设置缓存过期时间, 30天有效期
		rcm.setDefaultExpiration(60 * 60 * 24 * 30);// 秒
		return rcm;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		//设置连接
		template.setConnectionFactory(factory);
		// redis 开启事务 如果开启事务，则redis不会主动释放连接，需要手动释放
		template.setEnableTransactionSupport(false);
		//序列化value的值
		GenericFastJsonRedisSerializer serializer = new GenericFastJsonRedisSerializer();
		//指定白名单。只在此包下的类可转化, 其他路径禁止
		ParserConfig.getGlobalInstance().addAccept("com.wql.cloud");
		//设置键（key）的序列化采用StringRedisSerializer。
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		// 设置值（value）的序列化采用FastJsonRedisSerializer。
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);
		template.afterPropertiesSet();
		return template;
	}
	
	
	@Bean(name = "redisUtil")
    public RedisUtil getRedisUtil() {
    	RedisUtil redisUtil = new RedisUtil();
    	return redisUtil;
    }
	

}
