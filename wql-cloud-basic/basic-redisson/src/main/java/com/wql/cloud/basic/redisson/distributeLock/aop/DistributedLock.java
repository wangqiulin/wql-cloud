package com.wql.cloud.basic.redisson.distributeLock.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 当service层的方法体格式：
 * 
 * 一：参数为一个对象是（User user）userName为user的字段， 需要使用对象参数中的某个字段值作为锁
 * @DistributedLock(param="userName", tryLock=true)
 * public boolean method1(User user);
 * 
 * 
 * 二：参数为单个列举时，需要使用其中某个参数值锁时,使用argNum（表示使用第几个参数作为锁）
 * @DistributedLock(argNum=2, tryLock=true)
 * public boolean method2(Date payedTime, String orderNo, boolean notify);
 * 
 * 
 * 三：参数为一个对象是（User user）userName为user的字段， 需要使用对象参数中的某个字段值作为锁
 * @DistributedLock(param="userName", argNum=1, tryLock=true)
 * public boolean method3(User user, User user2, String str);
 * 
 * 优先看lockName属性，
 * 设置了(lockName)锁名称, 锁名称就是：前缀+lockName+后缀
 * 没有设置lockName，则看param+argNum
 * 
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
	
    /**
     * 锁的名称。
     * 如果lockName可以确定，直接设置该属性。
     */
    String lockName() default "";

    /**
     * lockName前缀
     */
    String lockNamePre() default "";
    
    /**
     * lockName后缀
     */
    String lockNamePost() default "lock";

    /**
     * 获得锁名时拼接前后缀用到的分隔符
     * @return
     */
    String separator() default ".";
    
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
    
    /**
     * 是否使用公平锁。
     * 公平锁即先来先得。
     */
    boolean fairLock() default false;
    
    /**
     * 是否使用尝试锁。
     */
    boolean tryLock() default false;
    
    /**
     * 最长等待时间。
     * 该字段只有当tryLock()返回true才有效。
     */
    long waitTime() default 30L;
    
    /**
     * 锁超时时间。
     * 超时时间过后，锁自动释放。
     * 建议：
     *   尽量缩简需要加锁的逻辑。
     */
    long leaseTime() default 5L;
    
    /**
     * 时间单位。默认为秒。
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
