package com.wql.cloud.basic.cat.aop;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.wql.cloud.basic.cat.CatTransaction;
import com.wql.cloud.basic.cat.method.CatMethodCache;
import com.wql.cloud.basic.cat.method.CatMethodTransaction;

/**
 * 大众点评cat aop代理
 */
@Aspect
@Component
public class CatAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(CatAspect.class);
	
    /**
     * 类级别的 transaction
     */
    @Around("execution(* com.wql.cloud.*.service..*.*(..))")
    public Object CatTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        //Class<? extends Object> object = joinPoint.getTarget().getClass();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        //类名+方法名
        String name = className + "." + methodName;
        if(joinPoint.getTarget().getClass().isAnnotationPresent(CatTransaction.class)){
            return aroundTransaction(className, name, joinPoint);
        }else if(joinPoint.getTarget().getClass().isAnnotationPresent(CatMethodCache.class)){
            return CatMethodCache(joinPoint);
        } else {
            return  joinPoint.proceed();
        }
    }

    /**
     * 拦截方法级别的
     */
    @Around("@annotation(com.wql.cloud.basic.cat.method.CatMethodTransaction)")
    public Object CatMethodTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<? extends Object> object = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Method[] methods = object.getMethods();
        Method method = null;
        for (Method m : methods) {
            if(m.getName().equals(methodName)){
                 method = m;
            }
        }
        CatMethodTransaction catMethodTransaction = method.getAnnotation(CatMethodTransaction.class);
        //注解有值的优先以注解的值
        String type = catMethodTransaction.type();
        if (StringUtils.isEmpty(type)) {
        	type = object.getSimpleName();
        }
        String name = catMethodTransaction.name();
        //类名+方法名
        if (StringUtils.isEmpty(name)) {
        	name = type + "." + methodName;
        }
        return aroundTransaction(type, name, joinPoint);
    }

    /**
     * 缓存测试
     */
    @Around("@annotation(com.wql.cloud.basic.cat.method.CatMethodCache)")
    public Object CatMethodCache(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<? extends Object> object = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Method[] methods = object.getMethods();
        Method method=null;
        for (Method m : methods) {
            if(m.getName().equals(methodName)){
                method = m;
            }
        }
        CatMethodCache catMethodCache = method.getAnnotation(CatMethodCache.class);
        //类名+方法名
        return aroundCache(catMethodCache,joinPoint);
    }

    private Object aroundCache(CatMethodCache catMethodTransaction, ProceedingJoinPoint joinPoint) throws Throwable {
        //注解有值的优先以注解的值
        String type = catMethodTransaction.type();
        String name = catMethodTransaction.name();
        Object result = null;
        Transaction transaction = null;
        //不让cat异常导致业务异常
        try {
            transaction = Cat.getProducer().newTransaction(type, name);
        } catch (Exception e) {
        	logger.error("Cat.getProducer().newTransaction Error", e);
        }
        try {
        	logger.info("大众点评cat拦截 CatMethodCache ：type="+type+";name="+name);
            result = joinPoint.proceed();
            if (transaction != null) {
                transaction.setStatus(Transaction.SUCCESS);
                if(joinPoint.getArgs().length > 0) {
                	transaction.addData("key="+joinPoint.getArgs()[0]+";"+"value="+ JSONObject.toJSON(result==null?new Object():result).toString());
                }
            }
        } catch (Throwable throwable) {
            if (transaction != null) {
            	transaction.setStatus(throwable);
            }
            logger.error("aroundTransaction exception", throwable);
            throw throwable;
        } finally {
            if (transaction != null) {
            	transaction.complete();
            }
        }
        return result;
    }

    private Object aroundTransaction(String type, String name, ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Transaction transaction = null;
        //不让cat异常导致业务异常
        try {
            transaction = Cat.getProducer().newTransaction(type, name);
        } catch (Exception e) {
        	logger.error("Cat.getProducer().newTransaction Error", e);
        }
        try {
        	logger.info("--->【大众点评cat拦截：type={}; name={}】", type, name);
            result = joinPoint.proceed();
            if (transaction != null) {
            	transaction.setStatus(Transaction.SUCCESS);
            }
        } catch (Throwable throwable) {
            if (transaction != null) {
            	transaction.setStatus(throwable);
            }
            logger.error("aroundTransaction exception", throwable);
            throw throwable;
        } finally {
            if (transaction != null) {
            	transaction.complete();
            }
        }
        return result;
    }
    
}
