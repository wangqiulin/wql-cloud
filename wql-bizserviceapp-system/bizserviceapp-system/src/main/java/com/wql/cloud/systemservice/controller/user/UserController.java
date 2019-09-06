package com.wql.cloud.systemservice.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.systemservice.pojo.res.UserResource;
import com.wql.cloud.systemservice.service.user.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/system/getUserResource")
	public DataResponse<List<UserResource>> getUserResource(@RequestParam(value = "userCode", required = true) String userCode){
		return DataResponse.success(userService.getUserResource(userCode));
	}
	
}
