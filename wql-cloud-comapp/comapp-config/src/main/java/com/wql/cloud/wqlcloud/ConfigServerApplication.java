package com.wql.cloud.wqlcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 *
 * @author wangqiulin
 * @date 2018年5月16日
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigServerApplication {

	 public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
     }
	
}
