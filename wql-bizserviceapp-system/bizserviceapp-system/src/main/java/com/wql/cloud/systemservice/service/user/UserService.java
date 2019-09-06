package com.wql.cloud.systemservice.service.user;

import java.util.List;

import com.wql.cloud.systemservice.pojo.res.UserResource;

public interface UserService {

	/**
	 * 获取用户所属资源
	 * 
	 * @param userCode
	 * @return
	 */
	List<UserResource> getUserResource(String userCode);

}
