package com.wql.cloud.basic.limit.user;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import com.google.common.collect.ImmutableList;
import com.wql.cloud.basic.limit.IPUtil;
import com.wql.cloud.basic.redis.util.RedisUtil;

/**
 * @UserLimit(period = 60, count = 1, prefix = "test:", param = "appId", argNum = 1)
 * public DataResponse<Void> test3(@RequestBody CreatePayOrderReq req) {
 * 		
 * }
 * @author wangqiulin
 *
 */
@Aspect
@Configuration
public class UserLimitAspect {

	private static final Logger logger = LoggerFactory.getLogger(UserLimitAspect.class);

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private RedisUtil redisService;
	 
    @Around("execution(public * *(..)) && @annotation(com.wql.cloud.basic.limit.user.UserLimit)")
    public Object interceptor(ProceedingJoinPoint pjp) {
    	Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        UserLimit limitAnnotation = method.getAnnotation(UserLimit.class);
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();
        //锁
        String key = null;
        LimitType limitType = limitAnnotation.limitType();
        switch (limitType) {
            case IP:
                key = IPUtil.getIpAdrress(request);
                break;
            case CUSTOMER:
            	if (args.length > 0) {
            		String param = limitAnnotation.param();
            		int argNum = limitAnnotation.argNum();
                    if (StringUtils.isNotBlank(param)) {
                        Object arg;
                        if (argNum > 0) {
                            arg = args[argNum - 1];
                        } else {
                            arg = args[0];
                        }
                        key = String.valueOf(getParam(arg, param));
                    } else if (argNum > 0) {
                    	key = args[argNum - 1].toString();
                    }
                } else {
                	throw new RuntimeException("limit-lock not param");
                }
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix(), key));
        try {
            String luaScript = buildLuaScript();
            RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
            Number count = redisService.execute(redisScript, keys, limitCount, limitPeriod);
            logger.info("Access try count is {} for key = {}", count, key);
            if (count != null && count.intValue() <= limitCount) {
                return pjp.proceed();
            } else {
                throw new RuntimeException("You have been dragged into the blacklist");
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
            	if("You have been dragged into the blacklist".equals(e.getLocalizedMessage())) {
            		throw new RuntimeException("请求频繁，请稍后重试");
            	} 
            }
            throw new RuntimeException("server exception");
        }
    }

    
    /**
     * 限流 脚本
     *
     * @return lua脚本
     */
    public String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c");
        lua.append("\nc = redis.call('get',KEYS[1])");
        // 调用不超过最大值，则直接返回
        lua.append("\nif c and tonumber(c) > tonumber(ARGV[1]) then");
        lua.append("\nreturn c;");
        lua.append("\nend");
        // 执行计算器自加
        lua.append("\nc = redis.call('incr',KEYS[1])");
        lua.append("\nif tonumber(c) == 1 then");
        // 从第一次调用开始限流，设置对应键值的过期
        lua.append("\nredis.call('expire',KEYS[1],ARGV[2])");
        lua.append("\nend");
        lua.append("\nreturn c;");
        return lua.toString();
    }
    
    
    /**
     * 从方法参数获取数据
     *
     * @param param
     * @param arg 方法的参数数组
     * @return
     */
    public Object getParam(Object arg, String param) {
        if (StringUtils.isNotBlank(param) && arg != null) {
            try {
                Object result = PropertyUtils.getProperty(arg, param);
                return result;
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(arg + "没有属性" + param + "或未实现get方法。", e);
            } catch (Exception e) {
                throw new RuntimeException("", e);
            }
        }
        return null;
    }
    

}
