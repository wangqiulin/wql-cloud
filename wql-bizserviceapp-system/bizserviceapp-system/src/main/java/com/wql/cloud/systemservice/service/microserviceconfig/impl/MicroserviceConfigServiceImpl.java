package com.wql.cloud.systemservice.service.microserviceconfig.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wql.cloud.systemservice.mapper.microserviceconfig.MicroserviceConfigMapper;
import com.wql.cloud.systemservice.pojo.domain.microserviceconfig.MicroserviceConfig;
import com.wql.cloud.systemservice.pojo.req.RefreshMicroserviceConfigReq;
import com.wql.cloud.systemservice.service.microserviceconfig.MicroserviceConfigService;
import com.wql.cloud.tool.httpclient.HttpUtil;

@Service
public class MicroserviceConfigServiceImpl implements MicroserviceConfigService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private HttpUtil httpUtil;
	@Autowired
	private MicroserviceConfigMapper microserviceConfigMapper;
	
	@Override
	public String refresh(RefreshMicroserviceConfigReq req) {
		Assert.isTrue(StringUtils.isNotBlank(req.getServiceName()), "服务名不能为空");
		String refreshContent = null;
		//查询数据
		MicroserviceConfig record = new MicroserviceConfig();
		record.setServiceName(req.getServiceName());
		List<MicroserviceConfig> list = microserviceConfigMapper.select(record);
		for (MicroserviceConfig microserviceConfig : list) {
			//刷新
			refreshContent = httpUtil.jsonPost(microserviceConfig.getServiceUrl());
		}
		return refreshContent;
	}
	

}
