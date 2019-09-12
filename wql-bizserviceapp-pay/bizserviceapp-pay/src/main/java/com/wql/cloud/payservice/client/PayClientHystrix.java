package com.wql.cloud.payservice.client;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.payservice.pojo.domain.PayOrder;

@Component
public class PayClientHystrix implements PayClient {

	@Override
	public DataResponse<List<PayOrder>> queryList(PayOrder req) {
		return new DataResponse<>(BusinessEnum.SYSTEM_FAIL);
	}

	@Override
	public DataResponse<Void> save(PayOrder order) {
		return new DataResponse<>(BusinessEnum.SYSTEM_FAIL);
	}	
	
}
