package com.wql.cloud.systemservice.service.dept.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wql.cloud.systemservice.mapper.dept.DeptMapper;
import com.wql.cloud.systemservice.pojo.domain.dept.Dept;
import com.wql.cloud.systemservice.service.dept.DeptService;

@Service
@CacheConfig(cacheNames = {"system-dept-cache"})
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptMapper deptMapper;
	
	@Override
	@Cacheable(key = "targetClass")
	public List<Dept> getDeptList() {
		return deptMapper.selectAll();
	}

}
