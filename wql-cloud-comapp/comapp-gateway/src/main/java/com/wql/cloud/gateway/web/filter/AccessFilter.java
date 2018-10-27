package com.wql.cloud.gateway.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@Component
public class AccessFilter extends ZuulFilter {

	private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info("send 【{}】 request to 【{}】",request.getMethod(),request.getRequestURI());
        String accessToken = request.getHeader("Authorization");
        /*if(StringUtils.isBlank(accessToken)){
            logger.warn("accessToken is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.set("isSuccess", false);
            ctx.setResponseBody("unAuthrized");
        } else {
        	logger.info("accessToken： {}" ,accessToken);
            ctx.setSendZuulResponse(true);
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);
        }*/
        return null;
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

	@Override
	public int filterOrder() {
		return 1;
	}

	@Override
	public String filterType() {
		/**
		 * pre：路由之前 routing：路由之时 post： 路由之后 error：发送错误调用
		 */
		return "pre";
	}

}
