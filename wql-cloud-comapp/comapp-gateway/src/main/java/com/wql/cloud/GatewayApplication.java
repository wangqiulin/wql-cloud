package com.wql.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.wql.cloud.gateway.core.filter.ErrorFilter;
import com.wql.cloud.gateway.core.filter.PreConfigFilter;
import com.wql.cloud.gateway.core.filter.RequestFilter;
import com.wql.cloud.gateway.core.filter.ResponseFilter;

/**
 * 网关服务： 1.接口路由 2.拦截过滤
 * 
 * @author wangqiulin
 * @date 2018年5月15日
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication(exclude = {MailSenderAutoConfiguration.class})
@EnableHystrix
@EnableFeignClients
public class GatewayApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(GatewayApplication.class).web(true).run(args);
	}

    /**
     * 请求过滤器
     * 
     * @return
     */
    @Bean
    public RequestFilter requestFilter() {
        return new RequestFilter();
    }

    /**
     * 读取配置过滤器
     * 
     * @return
     */
    @Bean
    public PreConfigFilter PreConfigFilter() {
        return new PreConfigFilter();
    }

    /**
     * 响应过滤器
     * 
     * @return
     */
    @Bean
    public ResponseFilter responseFilter() {
        return new ResponseFilter();
    }

    /**
     * 错误过滤器
     * 
     * @return
     */
    @Bean
    public ErrorFilter ErrorFilter() {
        return new ErrorFilter();
    }

    /**
     * RestTemplate
     * 
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	
}
