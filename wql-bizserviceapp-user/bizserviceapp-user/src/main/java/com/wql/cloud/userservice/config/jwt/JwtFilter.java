package com.wql.cloud.userservice.config.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wql.cloud.tool.jwt.CheckResult;
import com.wql.cloud.tool.jwt.JwtUtil;

/**
 * JWT登录认证拦截器
 * @author wangqiulin
 */
public class JwtFilter implements Filter {

	private static final String OPTIONS = "OPTIONS";

//	@Autowired
//	private UserService userService;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
		//返回结果
		Map<String, String> resultMap = new HashMap<String, String>();
		//登录后携带的token
		String token = httpRequest.getHeader("Authorization");
		if (StringUtils.isBlank(token)) {
			resultMap.put("msg", "请登录");
			resultMap.put("code", "1000");
		} else {
			CheckResult checkResult = JwtUtil.validateJWT(token);
			if(checkResult.getSuccess()) {
//				Claims claims = checkResult.getClaims();
				//TODO 根据userId, 查询该用户的权限，以及接口是否需要登录等权限
//				String userId = claims.getId();
//				String userContent = claims.getSubject();
				chain.doFilter(request, response);
				return;
			} else {
				resultMap.put("msg", "登录失效");
				resultMap.put("code", "1001");
			}
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
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
		//TODO 可以从redis中获取资源是否需要登录
		//放行的url
		List<String> urlList = Arrays.asList("/swagger-ui.html", "/info", "/webjars", "/v2", "/swagger-resources", 
				"/user/register", "/user/login"
		);
		for (String patternUrl : urlList) {
			Pattern p = Pattern.compile(patternUrl);
			Matcher m = p.matcher(url);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}

}
