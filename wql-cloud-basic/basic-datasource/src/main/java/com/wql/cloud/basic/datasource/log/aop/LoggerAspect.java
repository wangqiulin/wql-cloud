package com.wql.cloud.basic.datasource.log.aop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
@ConditionalOnExpression("${log.aspect:true}")
public class LoggerAspect {

	public static final Logger log = LoggerFactory.getLogger(LoggerAspect.class);

	public static final String UNKNOWN = "unknown";
	public final String MAC_ADDRESS_PREFIX = "MAC Address = ";
	public final String LOOPBACK_ADDRESS = "127.0.0.1";

	private ThreadLocal<Long> startTimeMillis = new ThreadLocal<>();

	@Autowired
	private HttpServletRequest request;
	
	@Resource
    private ObjectMapper mapper;

    /**
     * 日志打印脱敏关键字段是否开启默认开启
     */
    @Value("${log.aspect.desensitization.enabled:true}")
    private boolean desensitizationEnabled;

    /**
     * 日志打印脱敏关键词
     */
    @Value("#{'${log.aspect.desensitization.words:password}'.split(',')}")
    private List<String> words;

    /**
     * 敏感替换词
     */
    @Value("${log.aspect.desensitization.replace:******}")
    private String replace;
	

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
			+ "@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void log() {}

	@Before("log()")
    public void before(JoinPoint joinPoint) {
        try {
            // 记录方法开始执行的时间
            startTimeMillis.set(System.currentTimeMillis());
            String ip = UNKNOWN;
            String mac = UNKNOWN;
            String submitMethod = request.getMethod();
            // 请求path
            String path = request.getRequestURI().substring(request.getContextPath().length()).split(";")[0];
            // 打印请求日志
            printRequestLog(joinPoint, ip, mac, submitMethod, path);
        } catch (Exception e) {
            log.warn("请求切面before日志打印失败", e);
        }
    }

    /**
     * 打印日志
     */
    private void printRequestLog(JoinPoint joinPoint, String ip, String mac, String submitMethod, String path) {
        StringBuilder sb = new StringBuilder("\n-----------------------begin-----------------------");
        sb.append("\nrequest info is -------->\n");
        String method = joinPoint.getSignature().getName();
        // sb.append("ip : ").append(ip).append("\n");
        // sb.append("mac : ").append(mac).append("\n");
        sb.append("url         : ").append(path).append("\n");
        sb.append("reqMethod   : ").append(submitMethod).append("\n");
        sb.append("Controller  : ").append(joinPoint.getTarget().getClass().getName()).append(".(")
                .append(joinPoint.getTarget().getClass().getSimpleName()).append(".java:1)");
        sb.append("\nMethod      : ").append(method).append("\n");
        try {
            sb.append("args        : ").append(printArgs(excludeArgs(joinPoint.getArgs())) + "\n");
        } catch (Exception e) {
            log.warn("controller请求参数序列化异常", e);
        }
        log.info(sb.toString());
    }

    /**
     * 打印参数日志
     */
    private String printArgs(Object obj) {
        String result = null;
        try {
            result = mapper.writeValueAsString(desensitizationArgs(obj));
        } catch (JsonProcessingException e) {
            log.warn("printArgs失败", e);
        }
        return result;
    }

    /**
     * 参数去敏感
     */
    @SuppressWarnings("all")
	private Object desensitizationArgs(Object obj) {
        Object result = obj;
        if (desensitizationEnabled) {
            if (obj != null) {
                try {
                    Object rs;
                    if (obj instanceof Collection || obj instanceof Map) {
                        rs = obj;
                    } else {
                        rs = mapper.readValue(mapper.writeValueAsString(obj), Object.class);
                    }
                    if (rs instanceof Collection) {
                        Collection temp = new ArrayList<>();
                        for (Object o : (Collection) rs) {
                            if (isBasicType(o)) {
                                temp.add(o);
                            } else {
                                temp.add(desensitizationArgs(o));
                            }
                        }
                        result = temp;
                    } else if (rs instanceof Map) {
                        Map<String, Object> temp = new LinkedHashMap<>();
                        ((Map<String, Object>) rs).forEach((k, v) -> {
                            if (isBasicType(v)) {
                                if (words.contains(k)) {
                                    temp.put(k, replace);
                                } else {
                                    temp.put(k, v);
                                }
                            } else {
                                temp.put(k, desensitizationArgs(v));
                            }
                        });
                        result = temp;
                    }
                } catch (IOException e) {
                    log.warn("对象转map失败", e);
                }
            }
        }
        return result;
    }

    private boolean isBasicType(Object obj) {
        if (obj instanceof Number
                || obj instanceof Date
                || obj instanceof String
                || obj instanceof Number[]
                || obj instanceof Date[]
                || obj instanceof String[]
                || obj instanceof Character[]
        ) {
            return true;
        }
        return false;
    }

    /**
     * 排除掉request,response对象
     */
    private Object[] excludeArgs(Object[] args) {
        return Stream.of(args).filter(
                arg -> !(arg instanceof ServletRequest || arg instanceof ServletResponse || arg instanceof HttpSession))
                .toArray();
    }

    @AfterReturning(pointcut = "log()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        try {
            // 记录方法执行完成的时间
            Long startTime = startTimeMillis.get();
            startTimeMillis.remove();
            StringBuilder sb = new StringBuilder("response info is --->(耗时：" + (System.currentTimeMillis() - startTime)
                    + "ms)\n" + printArgs(returnValue));
            sb.append("\n-----------------------end-----------------------");
            log.info(sb.toString());
        } catch (Exception e) {
            log.warn("请求切面afterReturning日志打印失败", e);
        }
    }

}
