package com.wql.cloud.gateway.web.filter;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wql.cloud.gateway.property.GatewayProperty;
import com.wql.cloud.tool.ip.IPUtil;

/**
 * ip白名单
 * 
 * @author wangqiulin
 *
 */
@Component
public class IPFilter extends ZuulFilter {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GatewayProperty gatewayProperty;
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();
		String ipAddr = IPUtil.getIpAdrress(req);
		logger.info("IP请求地址为：[{}]", ipAddr);
		
		//配置本地IP白名单，生产环境可放入数据库或者redis中
		List<String> ips = null;
		String whiteIps = gatewayProperty.whiteIps;
		if(StringUtils.isNotBlank(whiteIps)) {
			ips = Arrays.asList(whiteIps.split(","));
		}
		if (ips == null || !ips.contains(ipAddr)) {
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
        }
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

}
