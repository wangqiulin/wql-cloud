package com.wql.cloud.userservice.config.log;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 日志拦截器
 */
@ConditionalOnExpression("${log.enabled:true}")
@Component
public class LogHttpRequestInterceptor extends HandlerInterceptorAdapter {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 日志ID key
	 */
	@Value("${log.traceId:traceId}")
	private String traceId;

	/**
	 * 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 如果返回true
	 * 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String traceId = request.getHeader(this.traceId);
		if (StringUtils.isEmpty(traceId)) {
			traceId = java.util.UUID.randomUUID().toString().replaceAll("-", "");
			logger.info("traceId do not exist in the request header, new traceId:{}", traceId);
		} else {
			logger.info("traceId exist in the request traceId:{}", traceId);
		}
		MDC.put(this.traceId, traceId);
		// 返回给客户端
		response.setHeader(this.traceId, traceId);
		return true;
	}

	/**
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
	 * <p>
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 删除
		MDC.remove(traceId);
	}

}
