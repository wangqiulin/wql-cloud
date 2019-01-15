package com.wql.cloud.userservice.service;

import java.util.Map;
import java.util.TreeMap;

public interface IModelFactory {
	
	/**
	 * 构造统一下单请求
	 * 
	 * @param bizParams
	 * @return
	 */
    TreeMap<String, String> buildPlaceOrderRequset(Map<String, String> bizParams);
	
	/**
	 * 构造订单查询请求和关闭订单
	 * 
	 * @param bizParams
	 * @return
	 */
    TreeMap<String, String> buildQueryTradeRequset(Map<String, String> bizParams);
	
	/**
	 * 申请退款
	 * 
	 * @param bizParams
	 * @return
	 */
    TreeMap<String, String> buildRefundRequset(Map<String, String> bizParams);
    
    /**
	 * 退款结果查询
	 * 
	 * @param bizParams
	 * @return
	 */
    TreeMap<String, String> buildRefundQueryRequset(Map<String, String> bizParams);

}
