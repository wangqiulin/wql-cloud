package com.wql.cloud.userservice.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wql.cloud.basic.datasource.response.DataResponse;
import com.wql.cloud.basic.datasource.response.constant.ApiEnum;
import com.wql.cloud.userservice.pojo.domain.User;

@Component
public class UserClientHystrix implements UserClient {

	@Override
	public DataResponse<List<User>> queryList(User req) {
		return new DataResponse<>(ApiEnum.SYSTEM_FAIL);
	}	
	
}
