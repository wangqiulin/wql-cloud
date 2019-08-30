package com.wql.cloud.systemservice.service.microserviceconfig.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.wql.cloud.systemservice.pojo.req.RefreshMicroserviceConfigReq;
import com.wql.cloud.systemservice.service.microserviceconfig.MicroserviceConfigService;
import com.wql.cloud.tool.httpclient.HttpUtil;

@Service
public class MicroserviceConfigServiceImpl implements MicroserviceConfigService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private HttpUtil httpUtil;
	
	@Override
	public String refresh(RefreshMicroserviceConfigReq req) {
		String refreshContent = null;
		String serviceUrl = req.getServiceUrl();
		Assert.isTrue(StringUtils.isNotBlank(serviceUrl), "url不能为空");
		for (String url : StringUtils.split(serviceUrl, ";")) {
			//post请求
			refreshContent = httpUtil.jsonPost(url);
		}
		return refreshContent;
	}
	

}
