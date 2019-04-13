package com.wql.cloud.payservice.service;

import com.wql.cloud.payservice.pojo.domain.Order;

import java.util.List;
import com.github.pagehelper.PageInfo;

/**
 * Author wangqiulin
 * Date  2019-04-13
 */
public interface OrderService {

	/**
	 * 新增
	 * 
	 * @param order
	 * @return
	 */
	Integer save(Order order);

	/**
	 * 修改
	 * 
	 * @param order
	 * @return
	 */
	Integer update(Order order);

	/**
	 * 删除
	 * 
	 * @param order
	 * @return
	 */
	Integer delete(Order order);

	/**
	 * 查询
	 * 
	 * @param order
	 * @return
	 */
	Order query(Order order);
	
	/**
	 * 查询列表
	 * 
	 * @param order
	 * @return
	 */
	List<Order> queryList(Order order);

	/**
	 * 分页查询列表
	 * 
	 * @param page
	 * @param pageSize
	 * @param order
	 * @return
	 */
	PageInfo<Order> queryPageList(Integer page, Integer pageSize, Order order);

}
