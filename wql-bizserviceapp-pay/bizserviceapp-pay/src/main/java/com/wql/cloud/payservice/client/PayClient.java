package com.wql.cloud.payservice.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.payservice.pojo.domain.Order;

@FeignClient(value = "${feign.serviceId.pay}", fallback = PayClientHystrix.class)
public interface PayClient {

	@RequestMapping(value = "/order/queryList", method = RequestMethod.POST)
	DataResponse<List<Order>> queryList(@RequestBody Order req);

	@RequestMapping(value = "/order/save", method = RequestMethod.POST)
	public DataResponse<Void> save(@RequestBody Order req);
	
}
