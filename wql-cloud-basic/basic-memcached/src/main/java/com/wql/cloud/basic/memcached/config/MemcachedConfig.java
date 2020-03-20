package com.wql.cloud.basic.memcached.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;

@Configuration
public class MemcachedConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MemcachedConfig.class);

    @Autowired
    private MemcachedProperties memcachedProperties;

    @Bean(name = "memcachedClientBuilder")
    public MemcachedClientBuilder getBuilder() {
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(memcachedProperties.getServer());
        // 内部采用一致性哈希算法
        builder.setSessionLocator(new KetamaMemcachedSessionLocator());
        // 操作的超时时间
        builder.setOpTimeout(memcachedProperties.getOpTimeout());
        // 采用二进制传输协议（默认为文本协议）
        builder.setCommandFactory(new BinaryCommandFactory());
        // 设置连接池的大小
        builder.setConnectionPoolSize(memcachedProperties.getPoolSize());
        // 是否开起失败模式
        builder.setFailureMode(memcachedProperties.isFailureMode());
        return builder;
    }

    /**
     * 由Builder创建memcachedClient对象，并注入spring容器中
     * @param memcachedClientBuilder
     * @return
     */
    @Bean(name = "memcachedClient")
    public MemcachedClient getClient(@Qualifier("memcachedClientBuilder") MemcachedClientBuilder memcachedClientBuilder) {
        MemcachedClient client = null;
        try {
            client =  memcachedClientBuilder.build();
        } catch(Exception e) {
        	logger.info("exception happens when bulid memcached client{}",e.toString());
        }
       return client;
    }

}
