//package com.wql.cloud.userservice.config.jwt;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//
////@Configuration
//public class JwtConfig {
//
//	@Bean
//	public FilterRegistrationBean jwtFilter() {
//		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//		registrationBean.setFilter(new JwtFilter());
//		// 添加需要拦截的url
//		List<String> urlPatterns = new ArrayList<String>();
//		urlPatterns.add("/*");
//		registrationBean.addUrlPatterns(urlPatterns.toArray(new String[urlPatterns.size()]));
//		return registrationBean;
//	}
//	
//}
