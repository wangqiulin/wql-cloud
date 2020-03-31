package com.wql.cloud.payservice.aop.userlimit;

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
public @interface UserLimit {

	/**
     * 给定的时间段
     * 单位秒
     *
     * @return int
     */
    int period();

    /**
     * 最多的访问限制次数
     *
     * @return int
     */
    int count();
	
    /**
     * 类型
     *
     * @return LimitType
     */
    LimitType limitType() default LimitType.CUSTOMER;

    /**
     * Key的prefix
     *
     * @return String
     */
    String prefix() default "";
    
    /**
     * <pre>
     *     获取注解的方法参数列表的【某个参数对象的某个属性值】来作为lockName。因为有时候lockName是不固定的。
     *     当param不为空时，可以通过argNum参数来设置具体是参数列表的第几个参数，不设置则默认取第一个。
     * </pre>
     */
    String param() default "";
    
    /**
     * 将方法第argNum个参数作为锁
     */
    int argNum() default 0;
    
}

