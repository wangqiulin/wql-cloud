package com.wql.cloud.wqlcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *
 * @author wangqiulin
 * @date 2018年5月16日
 */
@SpringBootApplication
@EnableEurekaClient
@EnableConfigServer
public class ConfigServerApplication {

	 public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
     }
	
}
