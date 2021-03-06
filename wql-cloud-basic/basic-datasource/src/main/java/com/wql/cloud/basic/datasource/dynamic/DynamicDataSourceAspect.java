package com.wql.cloud.basic.datasource.dynamic;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 保证该AOP在@Transactional之前执行
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

	/**
	 * @Description 在方法执行之前执行 @annotation(ds) 会拦截有ds这个注解的方法即有 TargetDataSource这个注解的
	 */
	@Before("@annotation(ds)")
	public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
		String dsId = ds.name();
		if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
			logger.info("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
		} else {
			logger.info("Use DataSource : {} > {}", ds.name(), point.getSignature());
			DynamicDataSourceContextHolder.setDataSourceType(ds.name());
		}
	}

	/**
	 * @Description 在方法执行之后执行 @annotation(ds) 会拦截有ds这个注解的方法即有
	 */
	@After("@annotation(ds)")
	public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
		logger.info("Revert DataSource : {} > {}", ds.name(), point.getSignature());
		DynamicDataSourceContextHolder.clearDataSourceType();
	}
	
}
