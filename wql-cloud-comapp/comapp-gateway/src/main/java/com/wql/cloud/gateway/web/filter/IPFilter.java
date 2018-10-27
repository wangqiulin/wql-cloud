package com.wql.cloud.gateway.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * ip白名单
 * 
 * @author wangqiulin
 *
 */
@Component
public class IPFilter extends ZuulFilter {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();
		String ipAddr = this.getIpAddr(req);
		logger.info("IP请求地址为：[{}]", ipAddr);
		
		//TODO 配置本地IP白名单，生产环境可放入数据库或者redis中
		/*List<String> ips = new ArrayList<String>();
		ips.add("127.0.0.1");
		if (!ips.contains(ipAddr)) {
			logger.info("IP地址校验不通过");
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(403);
			ctx.set("isSuccess", false);
			ctx.setResponseBody("ip is forbidden");
		} else {
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);
            ctx.getZuulRequestHeaders().put("HTTP_X_FORWARDED_FOR", ipAddr);
        }*/
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		//过滤器顺序,数值越小优先级越高
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		//如果前一个过滤器的结果为true，则说明上一个过滤器成功了
        RequestContext ctx = RequestContext.getCurrentContext();
        if(null == ctx.get("isSuccess")) {
        	return true;
        }
        return (boolean) ctx.get("isSuccess");
	}

	/**
	 * 获取真实ip地址
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
