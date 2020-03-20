package com.wql.cloud.basic.redis.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 判断是否存在该key
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
	
	// -------------------设置功能---------------------//
	/**
	 * 永久设置
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 带有效时间的设置, 时间单位为秒
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void setWithExByS(String key, String value, long seconds) {
		redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 带有效时间的设置, 时间单位为毫秒
	 * 
	 * @param key
	 * @param value
	 * @param milliseconds
	 */
	public void setWithExByMS(String key, String value, long milliseconds) {
		redisTemplate.opsForValue().set(key, value, milliseconds, TimeUnit.MILLISECONDS);
	}

	/**
	 * 设置hash结构值
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void hashSet(String key, String hashKey, Object value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	/**
	 * 设置map值
	 * 
	 * @param key
	 * @param dataMap
	 */
	public void setAllMap(String key, Map<Object, Object> dataMap) {
		redisTemplate.opsForHash().putAll(key, dataMap);
	}

	/**
	 * 在hashKey已经存在的情况下, putIfAbsent下不会进入修改value
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void hashSetIfAbsent(String key, String hashKey, Object value) {
		redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
	}
	
	/**
	 * 设置失效日期
	 * 
	 * @param key
	 * @param timeout
	 * @param timeUnit
	 * @return
	 */
	public Boolean expire(String key, Integer timeout, TimeUnit timeUnit) {
		return redisTemplate.expire(key, timeout, timeUnit);
	}

	// -------------------获取功能---------------------//
	
	/**
	 * 根据key，获取值
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 根据key，获取值
	 * 
	 * @param key
	 * @return
	 */
	public String getStr(String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getObj(String key, Class<T> clazz) {
		return (T) redisTemplate.opsForValue().get(key);
	}

	public Object hashGet(String key, String hashKey) {
		return redisTemplate.opsForHash().get(key, hashKey);
	}

	public Map<Object, Object> getMapByKey(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	public List<Object> getList(String key) {
		return redisTemplate.boundListOps(key).range(0, getListSize(key));
	}

	public long getListSize(String key) {
        return redisTemplate.boundListOps(key).size();
    }
	
	
	/**
	 * 获取剩余有效时间
	 * 
	 * @param key
	 * @return
	 */
	public long getExpireByKey(String key) {
		return redisTemplate.opsForValue().getOperations().getExpire(key);
	}
	
	// -------------------删除功能---------------------//

	/**
	 * 指定key删除
	 * 
	 * @param key
	 */
	public void remove(String key) {
		redisTemplate.delete(key);
	}
	
	/**
	 * 指定批量key删除
	 * 
	 * @param keys
	 */
	public void remove(List<String> keys) {
		redisTemplate.delete(keys);
	}
	
	/**
	 * 前置模糊匹配删除
	 * 
	 * @param prexKey
	 */
	public void removeByPrex(String prexKey) {
		Set<String> keys = redisTemplate.keys(prexKey + "*");
		redisTemplate.delete(keys);
	}
	
	/**
	 * 后置模糊匹配删除
	 * 
	 * @param suffixKey
	 */
	public void removeBySuffix(String suffixKey) {
		Set<String> keys = redisTemplate.keys("*" + suffixKey);
		redisTemplate.delete(keys);
	}
	
	/** hash结构数据删除, 返回删除成功的个数 */
	public Long hashDel(String key, Object... hashKeys) {
		return redisTemplate.opsForHash().delete(key, hashKeys);
	}

	
	// -------------------自增长和自增减---------------------//
	
	/**
	 * 按1自增
	 * 
	 * @param key
	 * @return
	 */
	public long incr(String key) {
		return redisTemplate.opsForValue().increment(key, 1);
	}

	/**
	 * 自增 或 自减
	 * 
	 * @param key
	 * @param byNum 正数，自增；  负数，自减
	 * @return
	 */
	public long incrByNum(String key, Integer byNum) {
		return redisTemplate.opsForValue().increment(key, byNum);
	}


	public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
	
	
//	public void tx(String key) {
//		//开启事务
//		redisTemplate.setEnableTransactionSupport(true);
//		//监听key
//		redisTemplate.watch(key);
//		//标志事务开始
//		redisTemplate.multi();
//		//一系列操作......
//		//事务提交
//		redisTemplate.exec();
//		//事务回滚
//		redisTemplate.discard();
//		//数据发生变化的话,取消监听
//		redisTemplate.unwatch();
//	}
	

}
