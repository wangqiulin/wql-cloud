package com.wql.cloud.userservice.service;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.userservice.pojo.req.UserReq;

/**
 *
 * @author wangqiulin
 * @date 2018年5月10日
 */
public interface UserService {
	
	/**
	 * 新增
	 * @param req
	 * @return
	 */
	DataResponse saveUser(UserReq req);

	/**
	 * 查询列表
	 * @return
	 */
	DataResponse queryUserAll();

	/**
	 * 分页查询列表
	 * @return
	 */
	Object queryPageUser(Integer page, Integer pageSize);

	/**
	 * 根据id，查询记录
	 * @param id
	 * @return
	 */
	DataResponse queryUserById(Integer id);

	/**
	 * 根据id，修改记录
	 * @param id
	 * @return
	 */
	DataResponse updateUserById(Integer id);

	/**
	 * 根据id，删除记录
	 * @param id
	 * @return
	 */
	DataResponse deleteUserById(Integer id);


}
