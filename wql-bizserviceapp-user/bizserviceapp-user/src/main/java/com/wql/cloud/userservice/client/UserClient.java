package com.wql.cloud.userservice.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;

/**
 * 对其他服务提供接口，在此处添加
 * 
 * @author wangqiulin
 *
 */
@FeignClient(value = "${feign.serviceId.user}", fallback = UserClientHystrix.class)
public interface UserClient {

	@PostMapping(value="/user/query/all")
	DataResponse queryUserAll();
	
}
