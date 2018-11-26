package com.wql.cloud.userservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wql.cloud.basic.datasource.commonService.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.basic.redisson.distributeLock.aop.DistributedLock;
import com.wql.cloud.userservice.domain.User;
import com.wql.cloud.userservice.mapper.UserMapper;
import com.wql.cloud.userservice.service.UserService;

/**
 * 组合形式：@Cacheable(cacheNames="users", key="(#name).concat('-').concat(#password)")
 * 对象形式：@Cacheable(value="cacheName", key="#user.id")
 * 
 * 
 * @author wangqiulin
 *
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    @LoadBalanced
    private RestTemplate balancedRestTemplate;
	
	
	@Override
	@TargetDataSource(name = "read")
	@Cacheable(cacheNames="cache-users")
	public List<User> queryUserAll() {
		return this.queryList();
	}

	
	@Override
	@TargetDataSource(name = "read")
	@Cacheable(cacheNames="cache-users", key = "#id")
	public User queryUserById(Integer id) {
		return this.queryById(id);
	}

	
	@Override
	@DistributedLock(lockName="lock:test", tryLock=true)
	@CachePut(cacheNames="cache-users", key = "#id")
	public Object updateUserById(Integer id) {
		User record = new User();
		record.setId(id);
		record.setUserName("王秋林");
		return this.updateSelective(record);
	}

	
	@Override
	@CacheEvict(cacheNames="cache-users", key = "#id")
	public Object deleteUserById(Integer id) {
		return this.deleteById(id);
	}

}
