package com.wql.cloud.wqlcloud.redis.lock;

import javax.annotation.PostConstruct;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 *
 * @author wangqiulin
 * @date 2018年5月16日
 */
@Component
public class RedissonConnector {
	
    RedissonClient redisson;
    
    @PostConstruct
    public void init(){
        redisson = Redisson.create();
    }

    public RedissonClient getClient(){
        return redisson;
    }

}
