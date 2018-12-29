package com.wql.cloud.basic.cat.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 大众点评 cat 方法级别注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CatMethodEvent {
    public String type() ;
    public String name() ;
}
