package com.wql.cloud.basic.redis.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 判断是否存在该key
	 * 
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
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
	
	/**
	 * 获取剩余有效时间
	 * 
	 * @param key
	 * @return
	 */
	public long getExpireByKey(String key) {
		return redisTemplate.opsForValue().getOperations().getExpire(key);
	}
	
	
	//======================String结构============================//
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
		return stringRedisTemplate.opsForValue().get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getObj(String key, Class<T> clazz) {
		return (T) redisTemplate.opsForValue().get(key);
	}

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
	 * @param delta 正数，自增；  负数，自减
	 * @return
	 */
	public long incr(String key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	public double incr(String key, double delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}
	
	
	//=============================zset数据结构-用于排行榜=================================//
	
	/**
	 * 批量新增----排行榜数据
	 * 
	 * @param key
	 * @param dataList
	 * @return
	 */
	public long add(String key, List<ScoreRank> dataList) {
		Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
		DefaultTypedTuple<String> tuple = null;
		for (ScoreRank scoreRank : dataList) {
			tuple = new DefaultTypedTuple<String>(scoreRank.getValue(), scoreRank.getScore());
			tuples.add(tuple);
		}
		return stringRedisTemplate.opsForZSet().add(key, tuples);
	}
	
	/**
	 * 单个新增--排行榜数据
	 * 
	 * @param key
	 * @param value
	 * @param score
	 * @return
	 */
	public boolean add(String key, String value, double score) {
		return stringRedisTemplate.opsForZSet().add(key, value, score);	
	}
	
	/**
	 * 加分数（如果不存在该值，则从0开始）
	 * 
	 * @param key
	 * @param value
	 * @param delta
	 * @return
	 */
	public double incrementScore(String key, String value, double delta) {
		return stringRedisTemplate.opsForZSet().incrementScore(key, value, delta);
	}
	
	/**
	 * 获取排行列表
	 * 
	 * @param key
	 * @param rankNo
	 * @return
	 */
	public Set<String> reverseRange(String key, long min, long max) {
		return stringRedisTemplate.opsForZSet().reverseRange(key, min, max);
	}
	
	/**
	 * 获取排行列表和分数列表
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public Set<String> reverseRange(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
	}
	
	/**
	 * 获取个人排名
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long getRank(String key, Object value) {
		return stringRedisTemplate.opsForZSet().reverseRank(key, value);
	}
	
	/**
	 * 获取个人分数
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public double getScore(String key, Object value) {
		return stringRedisTemplate.opsForZSet().score(key, value);
	}
	
	/**
	 * 获取两个分数间的人数
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public long getCount(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().count(key, min, max);
	}
	
	/**
	 * 获取总数
	 * 
	 * @param key
	 * @return
	 */
	public long getSize(String key) {
		return stringRedisTemplate.opsForZSet().zCard(key);
	}
	

	/**
	 * 通过key/value删除
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public long remove(String key, Object... values) {
		return stringRedisTemplate.opsForZSet().remove(key, values);
	}
	
	
	/**
	 * 通过排名区间删除
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public long removeRange(String key, long start, long end) {
		return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
	}
	
	/**
	 * 通过分数区间删除
	 * 
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	public long removeRangeByScore(String key, double min, double max) {
		return stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
	}
	
	
	//=============================set数据结构=================================//
	
	/**
	 * 向集合key中添加元素，返回添加的个数
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public long setAdd(String key, String... values) {
		return stringRedisTemplate.opsForSet().add(key, values);
	}
	
	/**
	 * 获取set集合中的列表
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> setMembers(String key) {
		return stringRedisTemplate.opsForSet().members(key);
	}
	
	/**
	 * 随机取一个元素
	 * 
	 * @param key
	 * @return
	 */
	public String setRandomMember(String key) {
		return stringRedisTemplate.opsForSet().randomMember(key);
	}
	
	/**
	 * 随机取指定个数的元素(数据不重复)
	 * 
	 * @param key
	 * @param count
	 * @return
	 */
	public Set<String> setDistinctRandomMember(String key, long count) {
		return stringRedisTemplate.opsForSet().distinctRandomMembers(key, count);
	}
	
	/**
	 * 从集合中删除指定元素
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public long setRemove(String key, Object... values) {
		return stringRedisTemplate.opsForSet().remove(key, values);
	}
	
	
	/**
	 * 从集合中随机删除一个元素，并返回该元素
	 * 
	 * @param key
	 * @return
	 */
	public String setPop(String key) {
		return stringRedisTemplate.opsForSet().pop(key);
	}
	
	/**
	 * 将集合A中的指定元素移到集合B，移动后原集合中就不存在该数据了
	 * 
	 * @param keyA
	 * @param keyB
	 * @param value
	 */
	public void setMoveAtoB(String keyA, String keyB, String value) {
		stringRedisTemplate.opsForSet().move(keyA, value, keyB);
	}
	
	/**
	 * 获取集合大小
	 * 
	 * @param key
	 * @return
	 */
	public long setSize(String key) {
		return stringRedisTemplate.opsForSet().size(key);
	}
	
	/**
	 * 判断集合中是否存在该元素
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setIsMember(String key, Object value) {
		return stringRedisTemplate.opsForSet().isMember(key, value);
	}
	
	/**
	 * 集合A与集合B的交集
	 * 
	 * @param keyA
	 * @param keyB
	 * @return
	 */
	public Set<String> intersect(String keyA, String keyB) {
		return stringRedisTemplate.opsForSet().intersect(keyA, keyB);
	}
	
	/**
	 * 集合A与集合B的交集, 结果存储到集合stroreKey
	 * 
	 * @param keyA
	 * @param keyB
	 * @param stroreKey
	 * @return
	 */
	public long intersectAndStore(String keyA, String keyB, String stroreKey) {
		return stringRedisTemplate.opsForSet().intersectAndStore(keyA, keyB, stroreKey);
	}
	
	public long intersectAndStore(String keyA, List<String> keyB, String stroreKey) {
		return stringRedisTemplate.opsForSet().intersectAndStore(keyA, keyB, stroreKey);
	}
	
	
	/**
	 * 集合A与集合B的并集
	 * 
	 * @param keyA
	 * @param keyB
	 * @return
	 */
	public Set<String> union(String keyA, String keyB) {
		return stringRedisTemplate.opsForSet().union(keyA, keyB);
	}
	
	/**
	 * 集合A与集合B的并集, 结果存储到集合stroreKey
	 * 
	 * @param keyA
	 * @param keyB
	 * @param keyStore
	 * @return
	 */
	public long unionAndStore(String keyA, String keyB, String keyStore) {
		return stringRedisTemplate.opsForSet().unionAndStore(keyA, keyB, keyStore);
	}
	
	public long unionAndStore(String keyA, List<String> keyB, String keyStore) {
		return stringRedisTemplate.opsForSet().unionAndStore(keyA, keyB, keyStore);
	}
	
	
	/**
	 * 差集[在集合A中，但不在集合B中]
	 * 
	 * @param keyA
	 * @param keyB
	 * @return
	 */
	public Set<String> difference(String keyA, String keyB) {
		return stringRedisTemplate.opsForSet().difference(keyA, keyB);
	}
	
	/**
	 * 差集[在集合A中，但不在集合B中, 结果存储到keyStore中]
	 * 
	 * @param keyA
	 * @param keyB
	 * @param keyStore
	 * @return
	 */
	public long difference(String keyA, String keyB, String keyStore) {
		return stringRedisTemplate.opsForSet().differenceAndStore(keyA, keyB, keyStore);
	}
	
	public long difference(String keyA, List<String> keyB, String keyStore) {
		return stringRedisTemplate.opsForSet().differenceAndStore(keyA, keyB, keyStore);
	}
	
	
	//=============================hash数据结构=================================//
		
	/**
	 * 设置hash结构值
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void hashPut(String key, String hashKey, Object value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	/**
	 * 设置map值
	 * 
	 * @param key
	 * @param dataMap
	 */
	public void hashPutAll(String key, Map<Object, Object> dataMap) {
		redisTemplate.opsForHash().putAll(key, dataMap);
	}

	/**
	 * 在hashKey已经存在的情况下, putIfAbsent下不会进入修改value
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public boolean hashPutIfAbsent(String key, String hashKey, Object value) {
		return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
	}
	
	public Object hashGet(String key, String hashKey) {
		return redisTemplate.opsForHash().get(key, hashKey);
	}

	public List<Object> hashMultiGet(String key, Collection<Object> hashKeys) {
		return redisTemplate.opsForHash().multiGet(key, hashKeys);
	}
	
	
	public Map<Object, Object> getMapByKey(String key) {
		return redisTemplate.opsForHash().entries(key);
	}
	
	/** hash结构数据删除, 返回删除成功的个数 */
	public long hashDelete(String key, Object... hashKeys) {
		return redisTemplate.opsForHash().delete(key, hashKeys);
	}
	
	public boolean hashHasKey(String key, String hashKey) {
		return redisTemplate.opsForHash().hasKey(key, hashKey);
	}
	
	public long hashIncrement(String key, String hashKey, long delta) {
		return redisTemplate.opsForHash().increment(key, hashKey, delta);
	}
	
	public double hashIncrement(String key, String hashKey, double delta) {
		return redisTemplate.opsForHash().increment(key, hashKey, delta);
	}
	
	
	//=============================bound队列数据结构(先进后出)=================================//
	
	/**
	 * 单条数据压入
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long boundListLeftPush(String key, Object value) {
		return redisTemplate.boundListOps(key).leftPush(value);
	}
	
	/**
	 * 多条数据压入
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public long boundListLeftPushAll(String key, Object... values) {
		return redisTemplate.boundListOps(key).leftPushAll(values);
	}
	

	public long boundListSize(String key) {
        return redisTemplate.boundListOps(key).size();
    }
	
	/**
	 * 查询所有
	 * 
	 * @param key
	 * @return
	 */
	public List<Object> boundListAll(String key) {
		return redisTemplate.boundListOps(key).range(0, -1);
	}
	
	/**
	 * 取数据
	 * 
	 * @param key
	 * @return
	 */
	public Object boundListLeftPop(String key) {
        return redisTemplate.boundListOps(key).leftPop();
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
