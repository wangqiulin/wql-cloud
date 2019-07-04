package com.wql.cloud.userservice.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.payservice.client.PayClient;
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

	@Autowired
	private PayClient payClient;
	@Autowired
	private HttpServletResponse response;
	
	@Override
	@Transactional
//	@GlobalTransactional
//	@DistributedLock(param = "userName", tryLock=true)
	public Integer save(User req) {
		Integer saveSelective = this.saveSelective(req);
//		Order order = new Order();
//		order.setOrderNo("111000");
//		order.setGoodsName("测试分布式事务");
//		payClient.save(order);
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
		return this.getById(req.getId());
	}
	
	@Override
	@TargetDataSource(name = "read")
	public List<User> queryList(User req) {
		List<User> list = this.listByRecord(req);
//		try {
//			EasyPoiUtil.exportExcel("用户信息", "用户列表", "user-excel.xls", User.class, list, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return list;
	}

	@Override
	@TargetDataSource(name = "read")
	public PageInfo<User> queryPageList(User req) {
		return this.pageListByRecord(req.getPage(), req.getPageSize(), req);
	}

}
