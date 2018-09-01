package com.wql.cloud.server.eureka;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {
        new SpringApplicationBuilder(EurekaServerApplication.class)
        	.web(true)
        	.bannerMode(Banner.Mode.OFF)
        	.run(args);
    }
	
}
