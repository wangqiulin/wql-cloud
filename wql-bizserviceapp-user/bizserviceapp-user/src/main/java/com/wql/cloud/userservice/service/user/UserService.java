package com.wql.cloud.userservice.service.user;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.userservice.pojo.domain.User;

/**
 *
 * @author wangqiulin
 * @date 2018年5月10日
 */
public interface UserService {
	
	/**
	 * 注册
	 * 
	 * @param req
	 * @return
	 */
	String register(User req);
	
	/**
	 * 登录
	 * 
	 * @param req
	 * @return
	 */
	String login(User req);

	/**
	 * 修改
	 * 
	 * @param user
	 * @return
	 */
	Integer update(User user);

	/**
	 * 删除记录
	 * 
	 * @param user
	 * @return
	 */
	Integer delete(User user);

	/**
	 * 查询记录
	 * 
	 * @param user
	 * @return
	 */
	User query(User user);
	
	/**
	 * 查询列表
	 * 
	 * @param user
	 * @return
	 */
	List<User> queryList(User user);

	/**
	 * 分页查询列表
	 * 
	 * @param user
	 * @return
	 */
	PageInfo<User> queryPageList(User user);

}
