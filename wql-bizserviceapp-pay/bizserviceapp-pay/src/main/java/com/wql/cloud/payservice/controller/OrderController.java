package com.wql.cloud.payservice.controller;

import java.util.List;
import com.wql.cloud.payservice.pojo.domain.Order;
import com.wql.cloud.payservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import com.github.pagehelper.PageInfo;

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
		return orderService.save(order) == 1 ? new DataResponse<>(BusinessEnum.SUCCESS) : new DataResponse<>(BusinessEnum.FAIL);
	}
	
	
	@ApiOperation(value = "修改")
	@PostMapping("/order/update")
	public DataResponse<Void> update(@RequestBody Order order) {
		return orderService.update(order) > 0 ? new DataResponse<>(BusinessEnum.SUCCESS) : new DataResponse<>(BusinessEnum.FAIL);
	}
	
	
	@ApiOperation(value = "删除")
	@PostMapping("/order/delete")
	public DataResponse<Void> delete(@RequestBody Order order) {
		return orderService.delete(order) > 0 ? new DataResponse<>(BusinessEnum.SUCCESS) : new DataResponse<>(BusinessEnum.FAIL);
	}
	
	
	@ApiOperation(value = "查询列表")
	@PostMapping("/order/queryList")
	public DataResponse<List<Order>> queryList(@RequestBody Order order) {
		DataResponse<List<Order>> dr = new DataResponse<>(BusinessEnum.SUCCESS);
		dr.setData(orderService.queryList(order));
		return dr;
	}
	
	
	@ApiOperation(value = "分页查询列表")
	@PostMapping("/order/queryPageList")
	public DataResponse<PageInfo<Order>> queryPageList(@RequestBody Order order) {
		DataResponse<PageInfo<Order>> dr = new DataResponse<>(BusinessEnum.SUCCESS);
		dr.setData(orderService.queryPageList(order.getPage(), order.getPageSize(), order));
		return dr;
	}
	
	
	@ApiOperation(value = "查询记录")
	@PostMapping("/order/query")
	public DataResponse<Order> query(@RequestBody Order order) {
		DataResponse<Order> dr = new DataResponse<>(BusinessEnum.SUCCESS);
		dr.setData(orderService.query(order));
		return dr;
	}
	
}
