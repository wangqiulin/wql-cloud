package com.wql.cloud.userservice.config.fegin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class FeginSupportHeaderConfig {

	@Bean
	public RequestInterceptor getRequestInterceptor(){
	    return new FeignInterceptor();
	}	
	
}
