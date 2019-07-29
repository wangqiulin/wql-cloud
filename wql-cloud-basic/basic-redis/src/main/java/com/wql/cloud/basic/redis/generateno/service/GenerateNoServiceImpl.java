package com.wql.cloud.basic.redis.generateno.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.redis.generateno.FormNoConstants;
import com.wql.cloud.basic.redis.generateno.FormNoSerialUtil;
import com.wql.cloud.basic.redis.generateno.FormNoTypeEnum;
import com.wql.cloud.basic.redis.util.RedisUtil;

/**
 * 生成唯一订单号的工具类
 * 
 * @author wangqiulin
 *
 */
@Service
public class GenerateNoServiceImpl implements GenerateNoService {

	@Autowired
	private RedisUtil redisUtil;
	
	/**
     * 根据单据编号类型 生成单据编号
     *
     * @param formNoTypeEnum 单据编号类型
     */
	@Override
    public String generateFormNo(FormNoTypeEnum formNoTypeEnum) {
        //获得单号前缀
        //格式 固定前缀 +时间前缀 示例 ：YF20190101
        String formNoPrefix = FormNoSerialUtil.getFormNoPrefix(formNoTypeEnum);
        //获得缓存key
        String cacheKey = FormNoSerialUtil.getCacheKey(formNoPrefix);
        //获得当日自增数
        Long incrementalSerial = redisUtil.incr(cacheKey);
        //设置失效时间 7天
        redisUtil.expire(cacheKey, FormNoConstants.DEFAULT_CACHE_DAYS, TimeUnit.DAYS);
        //组合单号并补全流水号
        String serialWithPrefix = FormNoSerialUtil.completionSerial(formNoPrefix, incrementalSerial, formNoTypeEnum);
        //补全随机数
        return FormNoSerialUtil.completionRandom(serialWithPrefix, formNoTypeEnum);
    }
    
    
}
