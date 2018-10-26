package com.wql.cloud.basic.jwt.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wql.cloud.basic.jwt.properties.JwtPatternUrlProperties;
import com.wql.cloud.basic.jwt.properties.JwtProperties;
import com.wql.cloud.basic.jwt.util.JwtHelper;

import io.jsonwebtoken.Claims;

/**
 * JWT登录认证拦截器
 * 
 * @author wangqiulin
 *
 */
public class JwtFilter implements Filter {

	private static final String OPTIONS = "OPTIONS";

	@Autowired
	private JwtProperties jwtProperty;

	@Autowired
	private JwtPatternUrlProperties jwtPatternUrl;

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
		String auth = httpRequest.getHeader("Authorization");
		if (StringUtils.isNoneBlank(auth) && auth.length() > 7) {
			String HeadStr = auth.substring(0, 6).toLowerCase();
			if (HeadStr.compareTo("bearer") == 0) {
				Claims claims = JwtHelper.parseJWT(auth.substring(7, auth.length()), jwtProperty.getBase64Secret());
				if (claims != null) {
					//TODO 判断该用户是否合法
					
					chain.doFilter(request, response);
					return;
				}
			}
		}
		// 验证不通过
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");

		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("code", "1000");
		resultMap.put("msg", "请登录");
		httpResponse.getWriter().write(mapper.writeValueAsString(resultMap));
	}


	private boolean isInclude(String url) {
		for (String patternUrl : jwtPatternUrl.getUrlPatterns()) {
			Pattern p = Pattern.compile(patternUrl);
			Matcher m = p.matcher(url);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

}
