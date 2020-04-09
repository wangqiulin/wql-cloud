package com.wql.cloud.basic.security.config;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 系统405解决方案
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = 4191063465365719694L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
		response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        Map<String, String> resMap = new HashMap<>();
        if (authException instanceof BadCredentialsException) {
        	resMap.put("code", "failure");
    		resMap.put("message", authException.getMessage());
        } else {
        	resMap.put("code", "failure");
    		resMap.put("message", authException.getMessage());
        }
        String userJson = JSON.toJSONString(resMap, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
        OutputStream out = response.getOutputStream();
        out.write(userJson.getBytes("UTF-8"));
        out.flush();
	}

}
