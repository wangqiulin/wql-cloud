package com.wql.cloud.basic.limit.ip;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {
	
	/**
	 * 次数
	 * @return
	 */
	int limit() default 10;

	/**
	 * 单位秒
	 * @return
	 */
	int time() default 1;
	
}
