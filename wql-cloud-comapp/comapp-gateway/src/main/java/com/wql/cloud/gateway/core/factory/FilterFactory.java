package com.wql.cloud.gateway.core.factory;

import java.util.List;

import com.wql.cloud.gateway.core.filter.InnerFilter;

/**
 * 过滤器工厂接口
 */
public interface FilterFactory {

    /**
     * 根据apiKey获取过滤器列表
     * @param apiKey
     * @return
     */
    public List<InnerFilter> getFilterList(String apiKey);
    
    /**
     * 初始化api filter
     */
    public void initApiFilterMap();

}
