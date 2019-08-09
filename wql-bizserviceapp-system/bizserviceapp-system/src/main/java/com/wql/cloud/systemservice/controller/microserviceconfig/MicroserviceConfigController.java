package com.wql.cloud.systemservice.controller.microserviceconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.systemservice.pojo.req.RefreshMicroserviceConfigReq;
import com.wql.cloud.systemservice.service.microserviceconfig.MicroserviceConfigService;

@RestController
public class MicroserviceConfigController {

	@Autowired
	private MicroserviceConfigService microserviceConfigService;
	
	/**
	 * 刷新配置
	 */
	@PostMapping("/system/refresh")
	public DataResponse<String> refresh(@RequestBody RefreshMicroserviceConfigReq req){
		return DataResponse.success(microserviceConfigService.refresh(req));
	}
	
	
}
