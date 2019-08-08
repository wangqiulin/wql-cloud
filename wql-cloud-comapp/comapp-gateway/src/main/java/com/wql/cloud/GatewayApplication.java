package com.wql.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关服务： 1.接口路由 2.拦截过滤
 * 
 * @author wangqiulin
 * @date 2018年5月15日
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication(exclude = {MailSenderAutoConfiguration.class})
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
