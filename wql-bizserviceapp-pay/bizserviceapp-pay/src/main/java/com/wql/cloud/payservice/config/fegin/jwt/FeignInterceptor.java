package com.wql.cloud.payservice.config.fegin.jwt;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * JWT如何在Spring Cloud微服务系统中在服务相互调时传递
 * Feign在发送网络请求之前会执行以下的拦截器 
 * 
 * 在微服务系统中，为了保证微服务系统的安全,常常使用jwt来鉴权，但是服务内部的相互调用呢。有一个解决办法，采用Feign的拦截器。
 * 在Feign中开启了hystrix，hystrix默认采用的是线程池作为隔离策略。线程隔离有一个难点需要处理，即隔离的线程无法获取当前请求线程的Jwt，这用ThredLocal类可以去解决，但是比较麻烦，所以我才用的是信号量模式。
 * 
 * 1.在application.yml配置文件中使用一下配置：
 * 		hystrix.command.default.execution.isolation.strategy: SEMAPHORE
 * 
 * 2.写一个Feign的拦截器，Feign在发送网络请求之前会执行以下的拦截器
 * 
 * @author wangqiulin
 * 
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

	private static final String TOKEN_HEADER = "Authorization";

	@Override
	public void apply(RequestTemplate requestTemplate) {
		String token = getHeaders().get(TOKEN_HEADER);
		if (StringUtils.isNotBlank(token)) {
			requestTemplate.header(TOKEN_HEADER, token);
		}
	}
	
	/**
	 * 获取所有header内容
	 * @return
	 */
    private Map<String, String> getHeaders() {
    	//返回map
    	Map<String, String> map = new LinkedHashMap<>();
    	//获取当前request
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
	
}
