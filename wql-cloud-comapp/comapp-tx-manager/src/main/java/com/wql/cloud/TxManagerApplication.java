package com.wql.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagerServer //分布式事务管理server
public class TxManagerApplication {

	public static void main(String[] args) {
        SpringApplication.run(TxManagerApplication.class, args);
    }
	
}
