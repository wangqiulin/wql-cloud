package com.wql.cloud.userservice.client;

import org.springframework.stereotype.Component;

import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.userservice.pojo.domain.User;

@Component
public class UserClientHystrix implements UserClient {

	@Override
	public DataResponse queryList(User req) {
		return new DataResponse(BusinessEnum.SYSTEM_FAIL);
	}	
	
}
