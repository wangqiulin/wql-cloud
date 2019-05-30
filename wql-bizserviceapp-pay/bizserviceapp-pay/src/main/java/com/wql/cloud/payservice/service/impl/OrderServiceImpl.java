package com.wql.cloud.payservice.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.payservice.pojo.domain.Order;
import com.wql.cloud.payservice.service.OrderService;
import com.xxl.mq.client.consumer.IMqConsumer;
import com.xxl.mq.client.consumer.MqResult;
import com.xxl.mq.client.consumer.annotation.MqConsumer;

/**
 * Author wangqiulin
 * Date  2019-04-13
 */
@Service
@MqConsumer(topic = "topic_1")
public class OrderServiceImpl extends BaseService<Order> implements OrderService, IMqConsumer {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
	
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
	public PageInfo<Order> queryPageList(Order order) {
		return this.pageListByRecord(order.getPage(), order.getPageSize(), order);
	}

	@Override
	public MqResult consume(String data) throws Exception {
		logger.info("==========================> mq : " + data);
		return MqResult.SUCCESS;
	}
	
}
