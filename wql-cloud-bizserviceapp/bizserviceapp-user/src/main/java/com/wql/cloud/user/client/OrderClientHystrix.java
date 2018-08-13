package com.wql.cloud.user.client;

import org.springframework.stereotype.Component;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@Component
public class OrderClientHystrix implements OrderClient{

	@Override
	public String queryOrderByName(String name) {
		return "sorry, " + name;
	}

}
