package com.wql.cloud.userservice.client;

import org.springframework.stereotype.Component;

import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;

@Component
public class UserClientHystrix implements UserClient {

	@Override
	public DataResponse queryUserAll() {
		return new DataResponse(BusinessEnum.SYSTEM_FAIL);
	}
	
}
