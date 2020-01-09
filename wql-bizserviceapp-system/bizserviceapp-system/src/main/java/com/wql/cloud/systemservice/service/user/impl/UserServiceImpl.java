package com.wql.cloud.systemservice.service.user.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.wql.cloud.basic.redis.util.UniqueUtil;
import com.wql.cloud.systemservice.mapper.dept.DeptMapper;
import com.wql.cloud.systemservice.mapper.role.RoleMapper;
import com.wql.cloud.systemservice.mapper.user.UserMapper;
import com.wql.cloud.systemservice.mapper.user.UserRoleRelMapper;
import com.wql.cloud.systemservice.pojo.domain.user.User;
import com.wql.cloud.systemservice.pojo.req.user.UserAddReq;
import com.wql.cloud.systemservice.pojo.req.user.UserDeleteReq;
import com.wql.cloud.systemservice.pojo.req.user.UserLoginReq;
import com.wql.cloud.systemservice.pojo.req.user.UserUpdateReq;
import com.wql.cloud.systemservice.pojo.res.user.UserLoginRes;
import com.wql.cloud.systemservice.service.user.UserService;

@Service
@CacheConfig(cacheNames = {"system-user-resource-cache"})
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleRelMapper userRoleRelMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private UniqueUtil uniqueUtil;
	
	
	@Override
	public UserLoginRes userLogin(UserLoginReq req) {
		Assert.notNull(req, "请求参数不能为空");
		String webappKey = req.getWebappKey();
		String userName = req.getUserName();
		String userPwd = req.getUserPwd();
		Assert.isTrue(StringUtils.isNotBlank(webappKey), "应用key不能为空");
		Assert.isTrue(StringUtils.isNotBlank(userName), "用户名不能为空");
		Assert.isTrue(StringUtils.isNotBlank(userPwd), "密码不能为空");
		//查询用户
		User user = queryUser(webappKey, userName);
		Assert.notNull(user, "用户不存在");
		Assert.isTrue(StringUtils.equals(userPwd, user.getUserPwd()), "密码错误");
		//生成token
		UserLoginRes res = new UserLoginRes();
		res.setToken(null);
		return res;
	}

	
	@Override
	@Transactional
	public void addUser(UserAddReq req) {
		Assert.notNull(req, "请求参数不能为空");
		String webappKey = req.getWebappKey();
		String userName = req.getUserName();
		String userPwd = req.getUserPwd();
		Integer userState = req.getUserState();
		Assert.isTrue(StringUtils.isNotBlank(webappKey), "应用key不能为空");
		Assert.isTrue(StringUtils.isNotBlank(userName), "用户名不能为空");
		Assert.isTrue(StringUtils.isNotBlank(userPwd), "密码不能为空");
		//查询用户
		User user = queryUser(webappKey, userName);
		Assert.isNull(user, "用户名已存在");
		//新增
		user = new User();
		user.setUserCode(uniqueUtil.getUnique("SystemUser"));
		user.setUserName(userName);
		user.setUserPwd(userPwd);
		user.setUserState(userState);
		user.setWebappKey(webappKey);
		userMapper.insertSelective(user);
	}
	
	
	@Override
	public void updateUser(UserUpdateReq req) {
		Assert.notNull(req, "请求参数不能为空");
		String userCode = req.getUserCode();
		String userPwd = req.getUserPwd();
		Integer userState = req.getUserState();
		Assert.isTrue(StringUtils.isNotBlank(userCode), "用户code不能为空");
		//查询用户
		User user = new User();
		user.setUserCode(userCode);
		user = userMapper.selectOne(user);
		Assert.notNull(user, "用户不存在");
		//修改
		user.setUserPwd(userPwd);
		user.setUserState(userState);
		userMapper.updateByPrimaryKeySelective(user);
	}
	
	
	@Override
	public void deleteUser(UserDeleteReq req) {
		Assert.notNull(req, "请求参数不能为空");
		String userCode = req.getUserCode();
		Assert.isTrue(StringUtils.isNotBlank(userCode), "用户code不能为空");
		//查询用户
		User user = new User();
		user.setUserCode(userCode);
		user = userMapper.selectOne(user);
		Assert.notNull(user, "用户不存在");
		userMapper.deleteByPrimaryKey(user.getId());		
	}
	
	
	@Override
	public List<User> queryUserList(User req) {
		return userMapper.select(req);
	}
	

	@Override
	@CachePut(key = "targetClass + #p0") //更新缓存
//	@CacheEvict(key = "targetClass + #p0", allEntries=true)  //方法调用后清空所有缓存
//	@CacheEvict(key = "targetClass + #p0", beforeInvocation=true) //方法调用前清空所有缓存
	public void updateUserResource(String userCode) {
		
	}

	

	private User queryUser(String webappKey, String userName) {
		User record = new User();
		record.setWebappKey(webappKey);
		record.setUserName(userName);
		User user = userMapper.selectOne(record);
		return user;
	}
	
	
}
