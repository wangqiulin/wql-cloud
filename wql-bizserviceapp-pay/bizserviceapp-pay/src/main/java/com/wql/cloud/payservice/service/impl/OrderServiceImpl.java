package com.wql.cloud.payservice.service.impl;

import com.wql.cloud.payservice.pojo.domain.Order;
import com.wql.cloud.payservice.service.OrderService;
import org.springframework.stereotype.Service;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import java.util.List;
import org.springframework.util.Assert;
import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;

/**
 * Author wangqiulin
 * Date  2019-04-13
 */
@Service
public class OrderServiceImpl extends BaseService<Order> implements OrderService {

	@Override
	public Integer save(Order order) {
		return this.saveSelective(order);
	}

	@Override
	public Integer update(Order order) {
		return this.updateSelectiveById(order);
	}

	@Override
	public Integer delete(Order order) {
		Assert.notNull(order.getId(), "id为空");
		return this.removeById(order.getId());
	}
	
	@Override
	@TargetDataSource(name = "read")
	public Order query(Order order) {
		Assert.notNull(order.getId(), "id为空");
		return this.getById(order.getId());
	}
	
	@Override
	@TargetDataSource(name = "read")
	public List<Order> queryList(Order order) {
		return this.listByRecord(order);
	}

	@Override
	@TargetDataSource(name = "read")
	public PageInfo<Order> queryPageList(Integer page, Integer pageSize, Order order) {
		return this.pageListByRecord(page, pageSize, order);
	}
	
}
