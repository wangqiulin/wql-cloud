package com.wql.cloud.basic.log.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 添加日志拦截器
 */
@ConditionalOnExpression("${log.enabled:true}")
@Configuration
public class LogWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

	@Autowired
	private LogHttpRequestInterceptor logInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(logInterceptor).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
	
}