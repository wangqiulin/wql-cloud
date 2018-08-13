package com.wql.cloud.client.order;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @FeignClient中的value，调用哪个服务的接口
 * 
 * @author wangqiulin
 * @date 2018年5月15日
 */
@FeignClient(value = "${fegin.serviceId.order}", fallback = OrderClientHystrix.class)
public interface OrderClient {

	 @GetMapping("/order/queryOrderByName")
	 String queryOrderByName(@RequestParam(value = "name") String name);
	
}
