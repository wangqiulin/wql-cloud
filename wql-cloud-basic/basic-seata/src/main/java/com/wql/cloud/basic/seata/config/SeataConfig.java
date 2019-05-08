package com.wql.cloud.basic.seata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.seata.spring.annotation.GlobalTransactionScanner;

@Configuration
public class SeataConfig {

	@Value("${spring.application.name}")
	private String applicationId;

	/**
	 * 注册一个StatViewServlet
	 *
	 * @return global transaction scanner
	 */
	@Bean
	public GlobalTransactionScanner globalTransactionScanner() {
        GlobalTransactionScanner globalTransactionScanner = new GlobalTransactionScanner(
        		applicationId, "my_test_tx_group");
		return globalTransactionScanner;
	}
	
}
