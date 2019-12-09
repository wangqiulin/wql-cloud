package com.wql.cloud.basic.security.login;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.security.jwt.JwtUtil;
import com.wql.cloud.basic.security.jwt.UUIDGenerator;
import com.wql.cloud.basic.security.model.MyAuthenticationToken;
import com.wql.cloud.basic.security.model.UserInfo;

@RestController
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * 后台用户登录接口
	 */
	@RequestMapping("${login.path:/user/login}")
	public Map<String, Object> login(@RequestBody UserLoginReq userLoginReq) {
		Map<String, Object> resMap = new HashMap<>();
		try {
			// 用户验证
			MyAuthenticationToken authenticationToken = new MyAuthenticationToken(userLoginReq.getUsername(), userLoginReq.getPassword());
			final Authentication authentication = authenticationManager.authenticate(authenticationToken);
			
			// 记录用户信息
			SecurityContextHolder.getContext().setAuthentication(authentication);
			MyAuthenticationToken myAuthentication = (MyAuthenticationToken) authentication;
			
			//使用jwt，生成Token
			UserInfo userInfo = (UserInfo)myAuthentication.getPrincipal();
			String token = JwtUtil.createJWT(UUIDGenerator.getUUID(), userInfo.getUserCode(), 1000*60*60*24*30);
            Map<String,Object> map = new HashMap<>();
			map.put("token", token);
			
            //返回结果
            resMap.put("code", "success");
			resMap.put("message", "登录成功");
			resMap.put("data", map);
		} catch (BadCredentialsException e) {
			logger.warn("登陆失败",e);
			resMap.put("code", "reject_fail");
			resMap.put("message", e.getMessage());
		} catch (Exception ex) {
			logger.error("登陆失败", ex);
			resMap.put("code", "failure");
			resMap.put("message", "登录失败");
		}
		return resMap;
	}
	
}
