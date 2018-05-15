package com.wql.cloud.wqlcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.wqlcloud.client.order.OrderClient;

/**
 *
 * @author wangqiulin
 * @date 2018年5月15日
 */
@RestController
public class UserController {

	@Autowired
	private OrderClient orderClient;
	
	
	/**
	 * 调用order模块的接口
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/user/queryUserOrderByName",method = RequestMethod.GET)
    public String queryOrderByName(@RequestParam String name){
        return orderClient.queryOrderByName(name);
    }
	
	
	@RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String sayHi(@RequestParam String name){
        return "user===> hello, " + name;
    }
	
}
