package com.wql.cloud.payservice.aop.iplimit;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.basic.datasource.response.exception.myexp.ApiException;
import com.wql.cloud.basic.redis.util.RedisUtil;
import com.wql.cloud.tool.ip.IPUtil;

/**
 * 使用方法：
 * 		@RequestLimit(time = 100, limit = 1)
 * 
 * @author wangqiulin
 *
 */
@Aspect
@Component
public class RequestLimitAspect {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	RedisUtil redisService;

	/**
	 * 被此注解标明的方法会被代理
	 */
	@Pointcut("execution(public * *(..)) && @annotation(com.wql.cloud.payservice.aop.iplimit.RequestLimit)")
	public void limitPointCut() {
	}

	/**
	 * 环绕通知拿到方法上RequestLimit注解的2个属性值 以作比较
	 */
	@Around("limitPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		RequestLimit requestLimit = signature.getMethod().getAnnotation(RequestLimit.class);
		if (requestLimit != null) {
			String key = getCacheKey();
			int limit = requestLimit.limit();
			int time = requestLimit.time();
			cacheOrRefuse(key, limit, time);
		}
		return point.proceed();
	}

	/**
	 * ip+url作为资源的key
	 */
	private String getCacheKey() {
		String ipAdrress = IPUtil.getIpAdrress(request);
		String key = ipAdrress + ":" + request.getRequestURI();
		return key;
	}

	/**
	 * 达到次数 停止服务 Or 访问次数+1
	 */
	private void cacheOrRefuse(String key, int limit, int time) {
		try {
			Object count = redisService.get(key);
			if(count != null) {
				int countRequest = Integer.parseInt(count.toString());
				if (countRequest > limit) {
					throw new ApiException("failure", "请求频繁，请稍后重试");
				}
			}
			if (redisService.hasKey(key)) {
				long expireByKey = redisService.getExpireByKey(key);
				if(expireByKey == -1) { //防止死锁
					redisService.remove(key);
				} else {
					redisService.incr(key, 1);
				}
			} else {
				redisService.setWithExByS(key, 1, (long)time);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

}
