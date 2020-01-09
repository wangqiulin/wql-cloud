package com.wql.cloud.basic.security.config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wql.cloud.basic.security.jwt.JwtUtil;
import com.wql.cloud.basic.security.model.MyGrantedAuthority;
import com.wql.cloud.basic.security.provider.MyUserDetailServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

/**
 * 授权验证实现
 */
public class MyAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(MyAuthenticationTokenFilter.class);

    /**登录链接*/
    @Value("${login.path:/user/login}")
    private String loginPath;

    /**token存放位置*/
    @Value("${header.token:Authorization}")
    private String tokenHeader;

    @Autowired
    private MyUserDetailServiceImpl userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        log.info("Security Log: {}", request.getRequestURI());
        // 如果是登录页面
        if (request.getRequestURI().indexOf(loginPath) >= 0) {
            chain.doFilter(request, response);
            return;
        }
        String userToken = request.getHeader(this.tokenHeader);
        if (StringUtils.isBlank(userToken)) {
            userToken = request.getParameter(this.tokenHeader);
        }
        try {
        	if (StringUtils.isNotBlank(userToken)) {
                // 解析Token
                Claims claims = null;
                try {
                	claims = JwtUtil.parseJWT(userToken);
                } catch (ExpiredJwtException | SignatureException e) {
                    // Token 无效
                    tokenExpired(response);
                    return;
                }
                // 取得用户Code
                String userCode = claims.getSubject();
                if (StringUtils.isBlank(userCode)) {
                    // Token 无效
                    tokenExpired(response);
                    return;
                }
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userCode);
                    if (checkPermission(request, userDetails.getAuthorities())) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // 无权限
                        authenticationError(response);
                        return;
                    }
                    chain.doFilter(request, response);
                }
            } else {
                chain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            log.error("服务器异常", e);
            this.error(response);
            return;
        }
    }

    private void tokenExpired(HttpServletResponse response) throws IOException, UnsupportedEncodingException {
    	Map<String, String> resMap = new HashMap<>();
		resMap.put("code", "token_expired_fail");
		resMap.put("message", "Token 无效");
        responseMessage(response, resMap, HttpStatus.UNAUTHORIZED.value());
    }

    private void authenticationError(HttpServletResponse response) throws IOException, UnsupportedEncodingException {
    	Map<String, String> resMap = new HashMap<>();
		resMap.put("code", "reject_fail");
		resMap.put("message", "无权操作");
        responseMessage(response, resMap, HttpStatus.FORBIDDEN.value());
    }

    private void error(HttpServletResponse response) throws IOException, UnsupportedEncodingException {
    	Map<String, String> resMap = new HashMap<>();
		resMap.put("code", "error");
		resMap.put("message", "服务端异常");
        responseMessage(response, resMap, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void responseMessage(HttpServletResponse response, Map<String, String> resMap, int responseStatus)
            throws IOException, UnsupportedEncodingException {
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(responseStatus);
        String userJson = JSON.toJSONString(resMap, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
        OutputStream out = response.getOutputStream();
        out.write(userJson.getBytes("UTF-8"));
        out.flush();
    }

    private boolean checkPermission(HttpServletRequest request, Collection<? extends GrantedAuthority> list) {
        boolean hasPermission = false;
        for (GrantedAuthority ga : list) {
            if (ga instanceof MyGrantedAuthority) {
                MyGrantedAuthority urlGrantedAuthority = (MyGrantedAuthority) ga;
                String url = urlGrantedAuthority.getUrl();
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(url);
                if (matcher.matches(request)) {
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }

}
