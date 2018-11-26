package com.wql.cloud.basic.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * session共享
 * spring-session中间件需要依赖redis2.8.0以上版本，并且需要开启：notify-keyspace-events
 * 如果spring-session使用的是redis集群环境，且redis集群环境没有开启Keyspace notifications功能，则应用启动时会抛出enableRedisKeyspaceNotificationsInitializer异常
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400*30)
//maxInactiveIntervalInSeconds: 设置Session失效时间，使用Redis Session之后，原Boot的server.session.timeout属性不再生效
public class SessionConfig {
	
	/**redis集群部署时，需要以下配置*/
	/*@Bean  
    public static ConfigureRedisAction configureRedisAction() {  
        return ConfigureRedisAction.NO_OP;  
    }*/  
	
}
