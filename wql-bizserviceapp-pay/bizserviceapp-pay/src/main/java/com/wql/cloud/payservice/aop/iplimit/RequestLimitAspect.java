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
	@Pointcut("@annotation(com.wql.cloud.payservice.aop.iplimit.RequestLimit)")
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
		Object result = point.proceed();
		return result;

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
		if (redisService.hasKey(key)) {
			String count = redisService.get(key).toString();
			int countRequest = Integer.parseInt(count);
			if (countRequest > limit) {
				throw new ApiException("failure", "请求频繁，请稍后重试");
			} else {
				redisService.incrByNum(key, 1);
			}
		} else {
			redisService.setWithExByS(key, "1", (long)time);
		}
	}

	/**
	 * 获取实际访问者ip 注意 通过ha或者nginx等代理的ip除了最后一次的访问ip 如（ip1,ip2,ip3,）
	 * 有可能篡改ip1或p2，只有ip3无法篡改，这时候只能判断ip3， 所以可以根据实际业务可以拿用户信息（比如token里面获取的用户信息）作为访问记录
	 */
//	public static String getIpAddress(HttpServletRequest request) {
//		String ip = request.getHeader("x-forwarded-for");
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getHeader("WL-Proxy-Client-IP");
//		}
//		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//			ip = request.getRemoteAddr();
//		}
//		return ip;
//	}
	
}
