package com.wql.cloud.userservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.userservice.pojo.domain.User;
import com.wql.cloud.userservice.service.UserService;

/**
 * 分布式锁： @DistributedLock(lockName="lock:test", tryLock=true)
 * 
 * @author wangqiulin
 *
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

	@Override
	public Integer save(User req) {
		return this.saveSelective(req);
	}

	@Override
	public Integer update(User req) {
		return this.updateSelectiveById(req);
	}

	@Override
	public Integer delete(User req) {
		Assert.notNull(req.getId(), "id为空");
		return this.removeById(req.getId());
	}
	
	@Override
	@TargetDataSource(name = "read")
	public User query(User req) {
		Assert.notNull(req.getId(), "id为空");
		return this.getById(req.getId());
	}
	
	@Override
	@TargetDataSource(name = "read")
	public List<User> queryList(User req) {
		return this.listByRecord(req);
	}

	@Override
	@TargetDataSource(name = "read")
	public PageInfo<User> queryPageList(Integer page, Integer pageSize, User req) {
		return this.pageListByRecord(page, pageSize, req);
	}

}
