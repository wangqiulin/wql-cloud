package com.wql.cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.wql.cloud.basic.datasource.dynamic.DynamicDataSourceRegister;

@SpringBootApplication
@EnableEurekaClient
@Import({DynamicDataSourceRegister.class})
@MapperScan("com.wql.cloud.serviceapp.user.mapper")
@EnableFeignClients
public class UserServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(UserServiceApplication.class).web(true).run(args);
	}
	
}
