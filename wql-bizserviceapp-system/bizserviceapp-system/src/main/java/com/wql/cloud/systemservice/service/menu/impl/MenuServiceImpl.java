package com.wql.cloud.systemservice.service.menu.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wql.cloud.systemservice.mapper.menu.MenuMapper;
import com.wql.cloud.systemservice.mapper.resource.ResourceMapper;
import com.wql.cloud.systemservice.mapper.role.RoleResourceRelMapper;
import com.wql.cloud.systemservice.mapper.user.UserMapper;
import com.wql.cloud.systemservice.mapper.user.UserRoleRelMapper;
import com.wql.cloud.systemservice.pojo.domain.menu.Menu;
import com.wql.cloud.systemservice.pojo.domain.resource.Resource;
import com.wql.cloud.systemservice.pojo.domain.role.RoleResourceRel;
import com.wql.cloud.systemservice.pojo.domain.user.User;
import com.wql.cloud.systemservice.pojo.domain.user.UserRoleRel;
import com.wql.cloud.systemservice.pojo.req.menu.UserMenuReq;
import com.wql.cloud.systemservice.pojo.res.menu.UserMenuRes;
import com.wql.cloud.systemservice.service.menu.MenuService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@CacheConfig(cacheNames = {"system-userMenu-cache"})
public class MenuServiceImpl implements MenuService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserRoleRelMapper userRoleRelMapper;
	@Autowired
	private RoleResourceRelMapper roleResourceRelMapper;
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private MenuMapper menuMapper;

	@Override
	@Cacheable(key = "targetClass + #p0")
	public UserMenuRes getUserMenu(UserMenuReq req) {
		//查询用户
		User userRecord = new User();
		userRecord.setWebappKey(req.getWebappKey());
		userRecord.setUserName(req.getUserName());
		User user = userMapper.selectOne(userRecord);
		Assert.notNull(user, "用户不存在");
		//获取用户角色
		UserRoleRel record = new UserRoleRel();
		record.setUserCode(user.getUserCode());
		List<UserRoleRel> userRoleRelList = userRoleRelMapper.select(record);
		Assert.notEmpty(userRoleRelList, "用户没有任何角色");
		//获取角色资源
		Example example = new Example(RoleResourceRel.class);
		Criteria criteria = example.createCriteria();
		criteria.andIn("roleCode", userRoleRelList.stream().map(UserRoleRel::getRoleCode).collect(Collectors.toList()));
		List<RoleResourceRel> roleResourceRelList = roleResourceRelMapper.selectByExample(example);
		Assert.notEmpty(roleResourceRelList, "用户没有任何资源");
		//获取资源明细
		Example example2 = new Example(Resource.class);
		Criteria criteria2 = example2.createCriteria();
		criteria2.andEqualTo("resourceState", 1);
		criteria2.andIn("resourceCode", roleResourceRelList.stream().map(RoleResourceRel::getResourceCode).distinct().collect(Collectors.toList()));
		List<Resource> resourceList = resourceMapper.selectByExample(example2);
		//查询菜单
		Menu menu = new Menu();
		menu.setWebappKey(req.getWebappKey());
		menu.setMenuShow(1);
		menu = menuMapper.selectOne(menu);
		Assert.notNull(menu, "无菜单结构");
		
		//TODO 递归子菜单
		UserMenuRes res = new UserMenuRes();
		res.setMenuCode(menu.getMenuCode());
		res.setMenuName(menu.getMenuName());
		res.setMenuSort(menu.getMenuSort());
		res.setParentMenuCode(menu.getMenuParentCode());
		res.setChildren(null);
		
		return res;
	}
	
	
	
	
}
