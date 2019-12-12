package com.wql.cloud.basic.redis.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

/**
 * 获取唯一值
 * @author wangqiulin
 *
 */
@Component
public class UniqueUtil {

	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
    /**
     * 根据获取的自增数据,添加日期标识构造分布式全局唯一标识
     * 
     * @param changeNumPrefix
     * @return
     */
	public String getUnique(String changeNumPrefix) {
        String dateStr = LocalDate.now().format(dateTimeFormatter);
        Long value = incrementNum(changeNumPrefix + dateStr);
        return dateStr + StringUtils.leftPad(String.valueOf(value), 12, '0');
    }

    // 从redis中获取自增数据(redis保证自增是原子操作)
    private long incrementNum(String key) {
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        if (null == factory) {
            throw new RuntimeException("链接异常");
        }
        RedisAtomicLong redisAtomicLong = new RedisAtomicLong(key, factory);
        long increment = redisAtomicLong.incrementAndGet();
        if (1 == increment) {
            // 如果数据是初次设置,需要设置超时时间
            redisAtomicLong.expire(1, TimeUnit.DAYS);
        }
        return increment;
    }
	
}
