package com.wql.cloud.basic.log.fegin;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * feign方式服务调用拦截器
 */
@ConditionalOnExpression("${log.enabled:true}")
@Component
public class LogFeignRequestInterceptor implements RequestInterceptor {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 日志ID key
	 */
	@Value("${log.traceId:traceId}")
	private String traceId;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		String traceId = MDC.get(this.traceId);
		logger.debug("feign request before set log header :{}", traceId);
		requestTemplate.header(this.traceId, traceId);
	}
	
}