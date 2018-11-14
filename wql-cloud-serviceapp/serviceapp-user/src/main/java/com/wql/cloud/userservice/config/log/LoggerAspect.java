package com.wql.cloud.userservice.config.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
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

	private ThreadLocal<Long> startTimeMillis = new ThreadLocal<>();

	public final String MAC_ADDRESS_PREFIX = "MAC Address = ";
	public final String LOOPBACK_ADDRESS = "127.0.0.1";

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
	public void log() {
		
	}

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
     * @param joinPoint
     * @param ip
     * @param mac
     * @param submitMethod
     * @param path
     */
    private void printRequestLog(JoinPoint joinPoint, String ip, String mac, String submitMethod, String path) {
        StringBuilder sb = new StringBuilder(
                "\n-------------------------------------begin-------------------------------------");
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
     *
     * @param obj
     * @return
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
     *
     * @param obj
     * @return
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
     *
     * @param args
     * @return
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
            sb.append("\n-------------------------------------end-------------------------------------");
            log.info(sb.toString());
        } catch (Exception e) {
            log.warn("请求切面afterReturning日志打印失败", e);
        }
    }

    /**
     * 通过HttpServletRequest返回IP地址
     *
     * @param request HttpServletRequest
     * @return ip String
     * @throws Exception
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = "";
        try {
            ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

        } catch (Exception e) {

        }
        return ip;
    }

    /**
     * 通过IP地址获取MAC地址
     *
     * @param ip String,127.0.0.1格式
     * @return mac String
     * @throws Exception
     */
    public String getMACAddress(String ip) {
        String macAddress = "";
        try {
            String line = "";

            // 如果为127.0.0.1,则获取本地MAC地址。
            if (LOOPBACK_ADDRESS.equals(ip)) {
                InetAddress inetAddress = InetAddress.getLocalHost();
                // 貌似此方法需要JDK1.6。
                byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
                // 下面代码是把mac地址拼装成String
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    if (i != 0) {
                        sb.append("-");
                    }
                    // mac[i] & 0xFF 是为了把byte转化为正整数
                    String s = Integer.toHexString(mac[i] & 0xFF);
                    sb.append(s.length() == 1 ? 0 + s : s);
                }
                // 把字符串所有小写字母改为大写成为正规的mac地址并返回
                macAddress = sb.toString().trim().toUpperCase();
                return macAddress;
            }
            // 获取非本地IP的MAC地址
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line != null) {
                    int index = line.indexOf(MAC_ADDRESS_PREFIX);
                    if (index != -1) {
                        macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            log.error("getMACAddress error ", e);
        }
        return macAddress;
    }
	
	
}
