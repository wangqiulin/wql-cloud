package com.wql.cloud.systemservice.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.systemservice.pojo.res.UserResource;
import com.wql.cloud.systemservice.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "用户相关接口")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@ApiOperation("查询用户资源")
	@RequestMapping(value = "/system/getUserResource", method = RequestMethod.POST)
	public DataResponse<List<UserResource>> getUserResource(@RequestParam(value = "userCode", required = true) String userCode){
		return DataResponse.success(userService.getUserResource(userCode));
	}
	
	
	
	
	
}
