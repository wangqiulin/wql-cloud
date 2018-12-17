package com.wql.cloud.basic.cat.filter;

import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebFilter;

import com.dianping.cat.servlet.CatFilter;

/**
 * 大众点评cat客户端接入
 * @author wangqiulin
 *
 */
@WebFilter(filterName = "cat-filter", urlPatterns = "/*", dispatcherTypes = { DispatcherType.REQUEST,
		DispatcherType.FORWARD })
public class CatFilters extends CatFilter {
	
}
