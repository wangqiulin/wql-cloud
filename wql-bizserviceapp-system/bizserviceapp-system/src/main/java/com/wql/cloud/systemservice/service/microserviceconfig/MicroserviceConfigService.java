package com.wql.cloud.systemservice.service.microserviceconfig;

import com.wql.cloud.systemservice.pojo.req.RefreshMicroserviceConfigReq;

public interface MicroserviceConfigService {

	String refresh(RefreshMicroserviceConfigReq req);

}
