package com.wql.cloud.userservice.service;

import com.wql.cloud.basic.response.constant.DataResponse;

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
	DataResponse queryUserAll(String filePath);

	DataResponse queryUserById(Integer id);

	DataResponse updateUserById(Integer id);

	DataResponse deleteUserById(Integer id);

}
