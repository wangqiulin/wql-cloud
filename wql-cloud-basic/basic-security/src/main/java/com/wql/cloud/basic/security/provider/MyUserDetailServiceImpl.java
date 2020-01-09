package com.wql.cloud.basic.security.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.security.model.MyGrantedAuthority;
import com.wql.cloud.basic.security.model.UserInfo;

/**
 * TODO 取得用户信息
 */
@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
    	if (StringUtils.isBlank(userCode)) {
            return null;
        }
        //TODO 根据userCode，查询用户信息
        Map<String, String> dataMap = null;
        if ("0".equals(dataMap.get("userState"))) {
        	//TODO 用户已停用:
        	
        } else {
        	//TODO 根据用户code，查询用户资源
            List<String> permissionUrlList = null;
            
            return this.createUserInfo(dataMap, permissionUrlList);
        }
        return null;
    }

    
    public UserInfo createUserInfo(Map<String, String> dataMap, List<String> permissionUrlList) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUserCode(dataMap.get("userCode"));
		userInfo.setUserName(dataMap.get("userName"));
		userInfo.setUserState(dataMap.get("userState"));
		//授权资源
		List<MyGrantedAuthority> authorities = new ArrayList<>();
		for (String permissionUrl : permissionUrlList) {
			MyGrantedAuthority authority = new MyGrantedAuthority();
			authority.setUrl(permissionUrl);
			authorities.add(authority);
		}
		userInfo.setAuthorities(authorities);
		return userInfo;
	}
    
    
}
