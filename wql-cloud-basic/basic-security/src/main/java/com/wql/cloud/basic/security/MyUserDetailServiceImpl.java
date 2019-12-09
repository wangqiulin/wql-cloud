package com.wql.cloud.basic.security;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.security.exception.UserDisableException;
import com.wql.cloud.basic.security.model.UserInfoFactory;
import com.wql.cloud.basic.security.model.UserRes;

/**
 * TODO 取得用户信息
 */
@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(MyUserDetailServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        try {
            if (StringUtils.isBlank(userCode)) {
                return null;
            }
            //TODO 根据userCode，查询用户信息
            UserRes userRes = null;
            if ("0".equals(userRes.getUserState())) {
            	throw new UserDisableException("用户已停用:" + userCode);
            } 

            //TODO 根据用户code，查询用户资源
            List<String> permissionUrlList = null;
            return UserInfoFactory.createUserInfo(userRes, permissionUrlList);
        } catch (UserDisableException e) {
            throw e;
        } catch (Exception e) {
            log.error("MyUserDetailServiceImpl.loadUserByUsername 异常",e);
        }
        return null;
    }

}
