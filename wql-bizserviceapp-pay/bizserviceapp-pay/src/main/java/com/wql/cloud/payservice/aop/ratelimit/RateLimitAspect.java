package com.wql.cloud.payservice.aop.ratelimit;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;

/**
 * 限流 切面枚举实现
 * 
 * 使用方法：适用于秒杀限购接口
 * 	就是在你控制器上面加上你注解即可： @RateLimit(limintNum = 10)
 */
@Component
@Scope
@Aspect
public class RateLimitAspect {
	
	public static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    /**
     * 用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter)
     */
    private ConcurrentHashMap<String, RateLimiter> map = new ConcurrentHashMap<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

    private RateLimiter rateLimiter;

    @Autowired
    private HttpServletResponse response;

    //@Pointcut("execution (* com.wql.cloud.*.controller..*.*(..))")
    @Pointcut("@annotation(com.wql.cloud.payservice.aop.ratelimit.RateLimit)")
    public void serviceLimit() {
    }

    @Around("serviceLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Object obj = null;
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        RateLimit annotation = currentMethod.getAnnotation(RateLimit.class);
        //获取注解每秒加入桶中的token
        int limitNum = annotation.limintNum();
        // 注解所在方法名区分不同的限流策略
        String functionName = msig.getName();

        //获取rateLimiter
        if(map.containsKey(functionName)){
            rateLimiter = map.get(functionName);
        }else {
            map.put(functionName, RateLimiter.create(limitNum));
            rateLimiter = map.get(functionName);
        }
        try {
            if (rateLimiter.tryAcquire()) {
                //执行方法
                obj = joinPoint.proceed();
            } else {
            	Map<String, String> map = new HashMap<>();
            	map.put("code", "failure");
            	map.put("message", "系统繁忙，请稍后再试！");
            	
                //拒绝了请求（服务降级）
                String result = objectMapper.writeValueAsString(map);
                log.info("拒绝了请求：" + result);
                outErrorResult(result);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
    
    //将结果返回
    public void outErrorResult(String result) {
        response.setContentType("application/json;charset=UTF-8");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(result.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
