package com.wql.cloud.userservice.service;

import java.util.List;

import com.wql.cloud.userservice.domain.User;

/**
 *
 * @author wangqiulin
 * @date 2018年5月10日
 */
public interface UserService {

	/**
	 * 查询用户列表
	 * @return
	 */
	List<User> queryAll();

}
