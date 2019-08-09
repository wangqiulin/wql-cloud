package com.wql.cloud.systemservice.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wql.cloud.systemservice.service.api.ApiService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@JobHandler(value = "apiCacheJobHandler")
@Component
public class ApiCacheJobHandler extends IJobHandler {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApiService apiService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        try {
        	apiService.loadApiCache();
        } catch (Exception e) {
            XxlJobLogger.log(e);
            return FAIL;
        } 
        return SUCCESS;
    }
    
}
