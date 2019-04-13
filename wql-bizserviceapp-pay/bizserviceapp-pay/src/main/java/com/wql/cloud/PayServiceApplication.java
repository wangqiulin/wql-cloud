package com.wql.cloud;

import java.util.TimeZone;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wql.cloud.basic.datasource.dynamic.DynamicDataSourceRegister;

@SpringBootApplication
@EnableEurekaClient
@Import({DynamicDataSourceRegister.class})
@tk.mybatis.spring.annotation.MapperScan("com.wql.cloud.payservice.mapper")
@EnableFeignClients
public class PayServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(PayServiceApplication.class).web(true).run(args);
	}
	
	@LoadBalanced
    @Bean(value="loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
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
	
	/**
     * 为系统ObjectMapper设置时区(系统时区)
     */
    @Primary
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.createXmlMapper(false).build().setTimeZone(TimeZone.getDefault());
    }

}
