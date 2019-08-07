package com.wql.cloud.userservice.config.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wql.cloud.tool.jwt.CheckResult;
import com.wql.cloud.tool.jwt.JwtUtil;

import io.jsonwebtoken.Claims;

/**
 * JWT登录认证拦截器
 * @author wangqiulin
 */
public class JwtFilter implements Filter {

	private static final String OPTIONS = "OPTIONS";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (OPTIONS.equals(httpRequest.getMethod())) {
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		//访问地址
		String url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
		if (isInclude(url)) {
			// 如果是属于排除的URL，比如登录，注册，验证码等URL，则直接通行
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		
		Map<String, String> resultMap = new HashMap<String, String>();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		
		String auth = httpRequest.getHeader("Authorization");
		if (StringUtils.isNoneBlank(auth) && auth.length() > 7) {
			String HeadStr = auth.substring(0, 6).toLowerCase();
			if (HeadStr.compareTo("bearer") == 0) {
				//从header中获取token内容
				String token = auth.substring(7, auth.length());
				
				CheckResult checkResult = JwtUtil.validateJWT(token);
				if(checkResult.getSuccess()) {
					Claims claims = checkResult.getClaims();
					if (claims != null) {
						//TODO 判断该用户是否合法
						String userId = claims.getId();
						String userContent = claims.getSubject();
						
						chain.doFilter(request, response);
						return;
					}
				} else {
					resultMap.put("code", "1001");
					resultMap.put("msg", "登录失效");
				}
			} else {
				resultMap.put("code", "1000");
				resultMap.put("msg", "请登录");
			}
		} else {
			resultMap.put("code", "1000");
			resultMap.put("msg", "请登录");
		}
		ObjectMapper mapper = new ObjectMapper();
		httpResponse.getWriter().write(mapper.writeValueAsString(resultMap));
	}

	/**
	 * 放行的url
	 * 
	 * @param url
	 * @return
	 */
	private boolean isInclude(String url) {
		///swagger-ui.html,  /info,  /webjars,  /v2,  /swagger-resources
//		for (String patternUrl : jwtPatternUrl.getUrlPatterns()) {
//			Pattern p = Pattern.compile(patternUrl);
//			Matcher m = p.matcher(url);
//			if (m.find()) {
//				return true;
//			}
//		}
		return false;
	}

}
