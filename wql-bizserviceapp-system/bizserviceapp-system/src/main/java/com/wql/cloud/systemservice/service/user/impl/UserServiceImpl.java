package com.wql.cloud.systemservice.service.user.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wql.cloud.systemservice.mapper.dept.DeptMapper;
import com.wql.cloud.systemservice.mapper.resource.ResourceDetailMapper;
import com.wql.cloud.systemservice.mapper.resource.ResourceMapper;
import com.wql.cloud.systemservice.mapper.role.RoleMapper;
import com.wql.cloud.systemservice.mapper.role.RoleResourceRelMapper;
import com.wql.cloud.systemservice.mapper.user.UserMapper;
import com.wql.cloud.systemservice.mapper.user.UserRoleRelMapper;
import com.wql.cloud.systemservice.pojo.domain.resource.ResourceDetail;
import com.wql.cloud.systemservice.pojo.domain.role.RoleResourceRel;
import com.wql.cloud.systemservice.pojo.domain.user.UserRoleRel;
import com.wql.cloud.systemservice.pojo.res.UserResource;
import com.wql.cloud.systemservice.service.user.UserService;
import com.wql.cloud.tool.bean.BeanUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
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
	
	@Override
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

	
}
