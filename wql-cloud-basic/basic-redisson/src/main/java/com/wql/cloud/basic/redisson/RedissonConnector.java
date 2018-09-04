package com.wql.cloud.basic.redisson;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 *
 * @author wangqiulin
 * @date 2018年5月16日
 */
@Component
public class RedissonConnector {
	
	@Autowired
    private RedissonClient redisson;
    
    @PostConstruct
    public void init() throws IOException{
        redisson = Redisson.create(
			Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream())
		);
    }

    public RedissonClient getClient(){
        return redisson;
    }

}
