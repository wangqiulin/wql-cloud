package com.wql.cloud.systemservice.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.systemservice.pojo.res.menu.UserMenuRes;

@Component
public class SystemClientHystrix implements SystemClient {

	@Override
	public DataResponse<List<UserMenuRes>> getUserResource(String userCode) {
		return new DataResponse<>(BusinessEnum.SYSTEM_FAIL);
	}
	
	
}
