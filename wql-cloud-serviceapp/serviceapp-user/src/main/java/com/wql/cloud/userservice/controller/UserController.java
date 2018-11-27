package com.wql.cloud.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.response.constant.DataResponse;
import com.wql.cloud.userservice.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/user/query/all")
	public DataResponse queryUserAll() {
		return userService.queryUserAll();
	}
	
	@PostMapping("/user/query/{id}")
	public DataResponse queryUserById(@PathVariable("id") Integer id) {
		return userService.queryUserById(id);
	}
	
	@PostMapping("/user/update/{id}")
	public DataResponse updateUserById(@PathVariable("id") Integer id) {
		return userService.updateUserById(id);
	}
	
	@PostMapping("/user/delete/{id}")
	public DataResponse deleteUserById(@PathVariable("id") Integer id) {
		return userService.deleteUserById(id);
	}
	
}
