package com.wql.cloud.wqlcloud.service.impl;

import org.springframework.stereotype.Service;

import com.wql.cloud.wqlcloud.service.OrderService;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Override
	public String queryOrderByName(String name) {
		return "hi, " + name;
	}

}
