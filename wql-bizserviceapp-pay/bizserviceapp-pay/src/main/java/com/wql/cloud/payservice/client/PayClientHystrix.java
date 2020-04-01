package com.wql.cloud.payservice.client;

import org.springframework.stereotype.Component;

import com.wql.cloud.basic.datasource.response.DataResponse;
import com.wql.cloud.basic.datasource.response.constant.ApiEnum;
import com.wql.cloud.payservice.pojo.req.CreatePayOrderReq;
import com.wql.cloud.payservice.pojo.req.CreateRefundOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryPayOrderReq;
import com.wql.cloud.payservice.pojo.req.QueryRefundOrderReq;
import com.wql.cloud.payservice.pojo.res.CreatePayOrderRes;
import com.wql.cloud.payservice.pojo.res.CreateRefundOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryPayOrderRes;
import com.wql.cloud.payservice.pojo.res.QueryRefundOrderRes;

@Component
public class PayClientHystrix implements PayClient {

	@Override
	public DataResponse<CreatePayOrderRes> createPayOrder(CreatePayOrderReq req) {
		return new DataResponse<>(ApiEnum.SYSTEM_FAIL);
	}

	@Override
	public DataResponse<QueryPayOrderRes> queryPayOrder(QueryPayOrderReq req) {
		return new DataResponse<>(ApiEnum.SYSTEM_FAIL);
	}

	@Override
	public DataResponse<CreateRefundOrderRes> createRefundOrder(CreateRefundOrderReq req) {
		return new DataResponse<>(ApiEnum.SYSTEM_FAIL);
	}

	@Override
	public DataResponse<QueryRefundOrderRes> queryRefundOrder(QueryRefundOrderReq req) {
		return new DataResponse<>(ApiEnum.SYSTEM_FAIL);
	}
	
}
