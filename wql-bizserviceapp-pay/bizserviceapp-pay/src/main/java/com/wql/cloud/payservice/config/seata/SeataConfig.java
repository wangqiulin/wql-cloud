//package com.wql.cloud.payservice.config.seata;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import io.seata.spring.annotation.GlobalTransactionScanner;
//
///**
// * https://github.com/SLY1311220942/demo-seata-springcloud
// * 
// * @author wangqiulin
// *
// */
//@Configuration
//public class SeataConfig {
//
//	@Value("${spring.application.name}")
//	private String applicationId;
//
//	/**
//	 * 初始化seataXid过滤器
//	 * 
//	 * @return
//	 */
//	@Bean
//	public SeataXidFilter fescarXidFilter() {
//		return new SeataXidFilter();
//	}
//
//	/**
//	 * 初始化全局事务扫描
//	 * 
//	 * @return
//	 */
//	@Bean
//	public GlobalTransactionScanner globalTransactionScanner() {
//		return new GlobalTransactionScanner(applicationId, "my_test_tx_group");
//	}
//	
//	
//}
