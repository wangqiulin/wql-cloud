package com.wql.cloud.basic.redis.generateno.service;

import com.wql.cloud.basic.redis.generateno.FormNoTypeEnum;

/**
 * 生成唯一订单号的接口类
 * 
 * @author wangqiulin
 *
 */
public interface GenerateNoService {

	/**
	 * 生成唯一订单号
	 * 
	 * @param formNoTypeEnum
	 * @return
	 */
	public String generateFormNo(FormNoTypeEnum formNoTypeEnum);
	
}
