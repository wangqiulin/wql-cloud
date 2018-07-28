package com.wql.cloud.wqlcloud.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.wqlcloud.order.service.OrderService;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping("/order/queryOrderByName")
    public String queryOrderByName(@RequestParam String name){
        return orderService.queryOrderByName(name);
    }
	
	
}
