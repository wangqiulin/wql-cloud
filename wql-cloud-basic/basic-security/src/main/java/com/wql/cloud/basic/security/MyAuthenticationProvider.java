package com.wql.cloud.basic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.wql.cloud.basic.security.model.MyAuthenticationToken;
import com.wql.cloud.basic.security.model.UserInfo;
import com.wql.cloud.basic.security.model.UserRes;

/**
 * TODO 自定义验证
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private MyUserDetailServiceImpl userDetailService;
	
	/**
	 * 用户认证
	 */
	@Override
	public Authentication authenticate(Authentication arg0) throws AuthenticationException {
		MyAuthenticationToken authToken = (MyAuthenticationToken)arg0;
		String username = authToken.getName();
		String password = (String)authToken.getCredentials();
		
		//TODO 根据userCode 取得用户信息
		UserRes userRes = null;
		if(userRes == null) {
			// 登录失败
			throw new BadCredentialsException("操作失败");
		}
		
		// 登录成功,处理一系列逻辑
		UserInfo userInfo = (UserInfo)userDetailService.loadUserByUsername(userRes.getUserCode());
		if (userInfo==null) {
			throw new BadCredentialsException("用户权限不存在");
		}
		return new MyAuthenticationToken(userInfo, null, userInfo.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
