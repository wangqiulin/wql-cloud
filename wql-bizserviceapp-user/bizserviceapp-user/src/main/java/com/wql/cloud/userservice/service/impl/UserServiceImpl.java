package com.wql.cloud.userservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.payservice.client.PayClient;
import com.wql.cloud.payservice.pojo.domain.Order;
import com.wql.cloud.userservice.pojo.domain.User;
import com.wql.cloud.userservice.service.UserService;

import io.seata.spring.annotation.GlobalTransactional;


/**
 * 分布式锁： @DistributedLock(lockName="lock:test", tryLock=true)
 * 
 * @author wangqiulin
 *
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private PayClient payClient;
//	@Autowired
//	private UserMapper userMapper;
	
	@Override
	@Transactional
	@GlobalTransactional(timeoutMills = 5000, name = "my_test_tx_group")
	public Integer save(User req) {
		Order order = new Order();
		order.setOrderNo("test");
		order.setGoodsName("测试分布式事务");
		payClient.save(order);
		Integer saveSelective = this.saveSelective(req);
//		int i = 1/0;
		return saveSelective;
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
		User user = this.getById(req.getId());
		return user;
	}
	
	@Override
	@TargetDataSource(name = "read")
	public List<User> queryList(User req) {
		List<User> list = this.listByRecord(req);
		return list;
	}

	@Override
	@TargetDataSource(name = "read")
	public PageInfo<User> queryPageList(User req) {
		return this.pageListByRecord(req.getPage(), req.getPageSize(), req);
	}

}
