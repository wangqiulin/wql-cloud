package com.wql.cloud.systemservice.service.user;

import java.util.List;

import com.wql.cloud.systemservice.pojo.domain.user.User;
import com.wql.cloud.systemservice.pojo.req.user.UserAddReq;
import com.wql.cloud.systemservice.pojo.req.user.UserDeleteReq;
import com.wql.cloud.systemservice.pojo.req.user.UserLoginReq;
import com.wql.cloud.systemservice.pojo.req.user.UserUpdateReq;
import com.wql.cloud.systemservice.pojo.res.user.UserLoginRes;
import com.wql.cloud.systemservice.pojo.res.user.UserResource;

public interface UserService {
	
	/**
	 * 用户登录
	 * @param req
	 * @return
	 */
	UserLoginRes userLogin(UserLoginReq req);

	/**
	 * 添加用户
	 * @param req
	 */
	void addUser(UserAddReq req);
	
	/**
	 * 修改用户
	 * @param req
	 */
	void updateUser(UserUpdateReq req);
	
	/**
	 * 删除用户
	 * @param req
	 */
	void deleteUser(UserDeleteReq req);
	
	/**
	 * 查询列表
	 * @param req
	 * @return
	 */
	List<User> queryUserList(User req);
	
	/**
	 * 获取用户所属资源
	 * @param userCode
	 * @return
	 */
	List<UserResource> getUserResource(String userCode);

	/**
	 * 更新用户资源
	 * @param userCode
	 */
	void updateUserResource(String userCode);

	

	
}
