package com.wql.cloud.basic.security.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建Security用 User对象
 */
public class UserInfoFactory {

	/**
	 * 创建用户对象
	 * @return
	 */
	public static UserInfo createUserInfo(UserRes userRes, List<String> permissionUrlList) {
		UserInfo userInfo = new UserInfo(convertGrantedAuthority(permissionUrlList));
		userInfo.setUserCode(userRes.getUserCode());
		userInfo.setUserName(userRes.getUserName());
		userInfo.setUserPhone(userRes.getUserPhone());
		return userInfo;
	}

	/**
	 * 授权资源
	 * @param permissionUrlList
	 * @return
	 */
	private static List<MyGrantedAuthority> convertGrantedAuthority(List<String> permissionUrlList) {
		List<MyGrantedAuthority> list = new ArrayList<>();
		for (String permissionUrl : permissionUrlList) {
			MyGrantedAuthority authority = new MyGrantedAuthority();
			authority.setUrl(permissionUrl);
			list.add(authority);
		}
		return list;
	}
}
