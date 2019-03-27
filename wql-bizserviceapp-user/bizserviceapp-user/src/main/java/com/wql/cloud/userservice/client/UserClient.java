package com.wql.cloud.userservice.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.userservice.pojo.domain.User;

/**
 * 对其他服务提供接口，在此处添加
 * 
 * @author wangqiulin
 *
 */
@FeignClient(value = "${feign.serviceId.user}", fallback = UserClientHystrix.class)
public interface UserClient {

	@PostMapping(value="/user/queryList")
	DataResponse queryList(@RequestBody User req);
	
}
