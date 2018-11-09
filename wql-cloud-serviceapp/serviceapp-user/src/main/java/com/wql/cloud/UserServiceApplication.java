package com.wql.cloud;

import java.util.TimeZone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import com.wql.cloud.basic.datasource.dynamic.DynamicDataSourceRegister;

import feign.Logger;

@SpringBootApplication
@EnableEurekaClient
@Import({DynamicDataSourceRegister.class})
@MapperScan("com.wql.cloud.serviceapp.user.mapper")
@EnableFeignClients
public class UserServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(UserServiceApplication.class).web(true).run(args);
	}
	
	@LoadBalanced
    @Bean
    public RestTemplate loadBalanced() {
        return new RestTemplate();
    }
	
	/**
	 * fegin调用打印日志
	 * 
	       其次，在application.properties中要设定一行这样的代码：
	   logging.level.<你的feign client全路径类名>: DEBUG
	       这样对应的feign client就可以输出日志了。这里必须是DEBUG才能生效。
	 * @return
	 */
	@Bean
    public Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
	
	/**
	 * Jackson日期反序列化时区问题
	 * @return
	 */
	@Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("GMT+8"));
    }

}
