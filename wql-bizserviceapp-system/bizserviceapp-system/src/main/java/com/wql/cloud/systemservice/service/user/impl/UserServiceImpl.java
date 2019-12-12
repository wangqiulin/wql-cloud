package com.wql.cloud.systemservice.service.user.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.wql.cloud.basic.redis.util.UniqueUtil;
import com.wql.cloud.systemservice.mapper.dept.DeptMapper;
import com.wql.cloud.systemservice.mapper.resource.ResourceDetailMapper;
import com.wql.cloud.systemservice.mapper.resource.ResourceMapper;
import com.wql.cloud.systemservice.mapper.role.RoleMapper;
import com.wql.cloud.systemservice.mapper.role.RoleResourceRelMapper;
import com.wql.cloud.systemservice.mapper.user.UserMapper;
import com.wql.cloud.systemservice.mapper.user.UserRoleRelMapper;
import com.wql.cloud.systemservice.pojo.domain.resource.ResourceDetail;
import com.wql.cloud.systemservice.pojo.domain.role.RoleResourceRel;
import com.wql.cloud.systemservice.pojo.domain.user.User;
import com.wql.cloud.systemservice.pojo.domain.user.UserRoleRel;
import com.wql.cloud.systemservice.pojo.req.user.UserAddReq;
import com.wql.cloud.systemservice.pojo.req.user.UserDeleteReq;
import com.wql.cloud.systemservice.pojo.req.user.UserLoginReq;
import com.wql.cloud.systemservice.pojo.req.user.UserUpdateReq;
import com.wql.cloud.systemservice.pojo.res.user.UserLoginRes;
import com.wql.cloud.systemservice.pojo.res.user.UserResource;
import com.wql.cloud.systemservice.service.user.UserService;
import com.wql.cloud.tool.bean.BeanUtils;
import com.wql.cloud.tool.jwt.JwtUtil;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

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
	private RoleResourceRelMapper roleResourceRelMapper;
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private ResourceDetailMapper resourceDetailMapper;
	@Autowired
	private DeptMapper deptMapper;
	@Autowired
	private UniqueUtil uniqueUtil;
	
	
	@Override
	public UserLoginRes userLogin(UserLoginReq req) {
		Assert.notNull(req, "请求参数不能为空");
		String userName = req.getUserName();
		String userPwd = req.getUserPwd();
		Assert.isTrue(StringUtils.isNotBlank(userName), "用户名不能为空");
		Assert.isTrue(StringUtils.isNotBlank(userPwd), "密码不能为空");
		//查询用户
		User user = new User();
		user.setUserName(userName);
		user = userMapper.selectOne(user);
		Assert.notNull(user, "用户不存在");
		Assert.isTrue(StringUtils.equals(userPwd, user.getUserPwd()), "密码错误");
		//生成token
		String token = JwtUtil.createJWT(String.valueOf(user.getId()), user.getUserName(), 3600*1000);
		UserLoginRes res = new UserLoginRes();
		res.setToken(token);
		return res;
	}
	
	
	@Override
	@Transactional
	public void addUser(UserAddReq req) {
		Assert.notNull(req, "请求参数不能为空");
		String userName = req.getUserName();
		String userPwd = req.getUserPwd();
		Integer userState = req.getUserState();
		Assert.isTrue(StringUtils.isNotBlank(userName), "用户名不能为空");
		Assert.isTrue(StringUtils.isNotBlank(userPwd), "密码不能为空");
		//查询用户
		User user = new User();
		user.setUserName(userName);
		user = userMapper.selectOne(user);
		Assert.isNull(user, "用户名已存在");
		//新增
		user = new User();
		user.setUserCode(uniqueUtil.getUnique("SystemUser"));
		user.setUserName(userName);
		user.setUserPwd(userPwd);
		user.setUserState(userState);
		userMapper.insertSelective(user);
	}
	
	
	@Override
	public void updateUser(UserUpdateReq req) {
		Assert.notNull(req, "请求参数不能为空");
		String userCode = req.getUserCode();
		String userName = req.getUserName();
		String userPwd = req.getUserPwd();
		Integer userState = req.getUserState();
		Assert.isTrue(StringUtils.isNotBlank(userCode), "用户code不能为空");
		//查询用户
		User user = new User();
		user.setUserCode(userCode);
		user = userMapper.selectOne(user);
		Assert.notNull(user, "用户不存在");
		//修改
		user.setUserName(userName);
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
	@Cacheable(key = "targetClass + #p0") //放入缓存
	public List<UserResource> getUserResource(String userCode) {
		//获取用户角色
		UserRoleRel record = new UserRoleRel();
		record.setUserCode(userCode);
		List<UserRoleRel> userRoleRelList = userRoleRelMapper.select(record);
		Assert.notEmpty(userRoleRelList, "用户没有任何角色");
		//获取角色资源
		Example example = new Example(RoleResourceRel.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("roleCode", userRoleRelList.stream().map(UserRoleRel::getRoleCode).collect(Collectors.toList()));
		List<RoleResourceRel> roleResourceRelList = roleResourceRelMapper.selectByExample(example);
		Assert.notEmpty(roleResourceRelList, "用户没有任何资源");
		//获取资源明细
		Example example2 = new Example(ResourceDetail.class);
		Criteria criteria2 = example2.createCriteria();
		criteria2.andEqualTo("resourceDetailState", 1);
		criteria2.andIn("resourceDetailCode", roleResourceRelList.stream().map(RoleResourceRel::getResourceDetailCode).distinct().collect(Collectors.toList()));
		List<ResourceDetail> resourceDetailList = resourceDetailMapper.selectByExample(example2);
		//响应对象
		List<UserResource> userResourceList = BeanUtils.deepCopyByList(resourceDetailList, UserResource.class);
		return userResourceList;
	}


	@Override
	@CachePut(key = "targetClass + #p0") //更新缓存
//	@CacheEvict(key = "targetClass + #p0", allEntries=true)  //方法调用后清空所有缓存
//	@CacheEvict(key = "targetClass + #p0", beforeInvocation=true) //方法调用前清空所有缓存
	public void updateUserResource(String userCode) {
		
	}

	
	
	
	
}
