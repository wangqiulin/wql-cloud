package com.wql.cloud.wqlcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.wqlcloud.service.OrderService;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	
	@RequestMapping(value = "/order/queryOrderByName",method = RequestMethod.GET)
    public String queryOrderByName(@RequestParam String name){
        return orderService.queryOrderByName(name);
    }
	
	
	@RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return "order===> hello, " + name;
    }
	
	
}
