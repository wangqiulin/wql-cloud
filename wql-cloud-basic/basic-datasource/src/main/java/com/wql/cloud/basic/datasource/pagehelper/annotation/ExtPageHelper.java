package com.wql.cloud.basic.datasource.pagehelper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//整合mybatis分页插件
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ExtPageHelper {

	int page() default 1;
	
	int pageSize() default 10;
	
}
