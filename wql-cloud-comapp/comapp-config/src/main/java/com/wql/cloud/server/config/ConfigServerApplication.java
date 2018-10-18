package com.wql.cloud.server.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 1，@EnableDiscoveryClient注解是基于spring-cloud-commons依赖，并且在classpath中实现； 
   2，@EnableEurekaClient注解是基于spring-cloud-netflix依赖，只能为eureka作用；
       如果你的classpath中添加了eureka，则它们的作用是一样的。
 * 
 * @author wangqiulin
 * @date 2018年5月16日
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigServerApplication {

	 public static void main(String[] args) {
        new SpringApplicationBuilder(ConfigServerApplication.class)
        	.web(true)
        	.run(args);
     }
	
}
