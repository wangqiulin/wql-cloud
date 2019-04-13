package com.wql.cloud.userservice.config.fescar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;

@Configuration
public class FescarConfiguration {

	@Value("${spring.application.name}")
	private String applicationId;

	/**
	 * 注册一个StatViewServlet
	 *
	 * @return global transaction scanner
	 */
	@Bean
	public GlobalTransactionScanner globalTransactionScanner() {
		GlobalTransactionScanner globalTransactionScanner = new GlobalTransactionScanner(applicationId, "receivables");
		return globalTransactionScanner;
	}

}
