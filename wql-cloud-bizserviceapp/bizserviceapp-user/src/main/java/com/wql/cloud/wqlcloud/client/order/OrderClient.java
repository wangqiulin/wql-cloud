package com.wql.cloud.wqlcloud.client.order;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @FeignClient中的value，调用哪个服务的接口
 * 
 * @author wangqiulin
 * @date 2018年5月15日
 */
@FeignClient(value = "service-order", fallback = OrderClientHystrix.class)
public interface OrderClient {

	 @RequestMapping(value = "/order/queryOrderByName",method = RequestMethod.GET)
	 String queryOrderByName(@RequestParam(value = "name") String name);
	
}
