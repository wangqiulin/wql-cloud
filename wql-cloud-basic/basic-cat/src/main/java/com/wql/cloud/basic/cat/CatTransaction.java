package com.wql.cloud.basic.cat;


import java.lang.annotation.*;

/**
 * 大众点评cat Transaction注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface CatTransaction {
}
