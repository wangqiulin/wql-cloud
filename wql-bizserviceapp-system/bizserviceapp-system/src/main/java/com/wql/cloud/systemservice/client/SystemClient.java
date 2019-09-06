package com.wql.cloud.systemservice.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.systemservice.pojo.res.UserResource;

@FeignClient(value = "${feign.serviceId.system}", fallback = SystemClientHystrix.class)
public interface SystemClient {

	/**
	 * 获取用户资源
	 * 
	 * @param userCode
	 * @return
	 */
	@RequestMapping(value = "/system/getUserResource", method = RequestMethod.POST)
	DataResponse<List<UserResource>> getUserResource(@RequestParam(value = "userCode", required = true) String userCode);
	
}
