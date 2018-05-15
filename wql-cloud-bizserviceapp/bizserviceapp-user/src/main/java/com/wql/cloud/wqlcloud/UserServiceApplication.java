package com.wql.cloud.wqlcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableDiscoveryClient  //eureka客户端
@EnableFeignClients  //服务间调用
@EnableHystrix //容错机制
@EnableHystrixDashboard  //Hystrix 仪表盘, 访问地址：http://localhost:6001/hystrix 。   然后输入http://localhost:6001/hystrix.stream
public class UserServiceApplication {

	public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
	
}
