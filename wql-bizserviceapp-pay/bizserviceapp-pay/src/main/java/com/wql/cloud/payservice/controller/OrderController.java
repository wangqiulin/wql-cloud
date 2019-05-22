package com.wql.cloud.payservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.payservice.pojo.domain.Order;
import com.wql.cloud.payservice.service.OrderService;

import io.swagger.annotations.ApiOperation;

/**
 * Author wangqiulin
 * Date  2019-04-13
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

	@ApiOperation(value = "新增")
	@PostMapping("/order/save")
	public DataResponse<Void> save(@RequestBody Order order) {
		return orderService.save(order) == 1 ? DataResponse.success() : DataResponse.failure();
	}
	
	
	@ApiOperation(value = "修改")
	@PostMapping("/order/update")
	public DataResponse<Void> update(@RequestBody Order order) {
		return orderService.update(order) > 0 ? DataResponse.success() : DataResponse.failure();
	}
	
	
	@ApiOperation(value = "删除")
	@PostMapping("/order/delete")
	public DataResponse<Void> delete(@RequestBody Order order) {
		return orderService.delete(order) > 0 ? DataResponse.success() : DataResponse.failure();
	}
	
	
	@ApiOperation(value = "查询列表")
	@PostMapping("/order/queryList")
	public DataResponse<List<Order>> queryList(@RequestBody Order order) {
		return DataResponse.success(orderService.queryList(order));
	}
	
	
	@ApiOperation(value = "分页查询列表")
	@PostMapping("/order/queryPageList")
	public DataResponse<PageInfo<Order>> queryPageList(@RequestBody Order order) {
		return DataResponse.success(orderService.queryPageList(order.getPage(), order.getPageSize(), order));
	}
	
	
	@ApiOperation(value = "查询记录")
	@PostMapping("/order/query")
	public DataResponse<Order> query(@RequestBody Order order) {
		return DataResponse.success(orderService.query(order));
	}
	
}
