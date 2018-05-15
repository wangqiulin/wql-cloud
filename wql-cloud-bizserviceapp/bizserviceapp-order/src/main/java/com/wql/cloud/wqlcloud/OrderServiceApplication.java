package com.wql.cloud.wqlcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class OrderServiceApplication {
	
	public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
