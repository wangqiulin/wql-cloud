package com.wql.cloud.serviceapp.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

import com.wql.cloud.basic.datasource.dynamic.DynamicDataSourceRegister;

@SpringBootApplication
@EnableEurekaClient
@Import({DynamicDataSourceRegister.class})
public class UserServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(UserServiceApplication.class).web(true).run(args);
	}
	
}
