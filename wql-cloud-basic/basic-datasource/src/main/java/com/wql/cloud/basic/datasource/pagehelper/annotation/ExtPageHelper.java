package com.wql.cloud.basic.datasource.pagehelper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//整合mybatis分页插件
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ExtPageHelper {

	/**
	 * 页码，默认第1页
	 * 
	 * @return
	 */
	int page() default 1;
	
	/**
	 * 每页条数，默认每页10条
	 * 
	 * @return
	 */
	int pageSize() default 10;
	
}
