package com.wql.cloud.systemservice.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.systemservice.pojo.domain.user.User;
import com.wql.cloud.systemservice.pojo.req.user.UserAddReq;
import com.wql.cloud.systemservice.pojo.req.user.UserDeleteReq;
import com.wql.cloud.systemservice.pojo.req.user.UserLoginReq;
import com.wql.cloud.systemservice.pojo.req.user.UserUpdateReq;
import com.wql.cloud.systemservice.pojo.res.user.UserLoginRes;
import com.wql.cloud.systemservice.pojo.res.user.UserResource;
import com.wql.cloud.systemservice.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "用户相关接口")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@ApiOperation("用户登录")
	@RequestMapping(value = "/system/userLogin", method = RequestMethod.POST)
	public DataResponse<UserLoginRes> userLogin(@RequestBody UserLoginReq req){
		UserLoginRes res = userService.userLogin(req);
		return DataResponse.success(res);
	}
	
	
	@ApiOperation("用户添加")
	@RequestMapping(value = "/system/addUser", method = RequestMethod.POST)
	public DataResponse<Void> addUser(@RequestBody UserAddReq req){
		userService.addUser(req);
		return DataResponse.success();
	}
	
	
	@ApiOperation("用户修改")
	@RequestMapping(value = "/system/updateUser", method = RequestMethod.POST)
	public DataResponse<Void> updateUser(@RequestBody UserUpdateReq req){
		userService.updateUser(req);
		return DataResponse.success();
	}
	
	
	@ApiOperation("用户删除")
	@RequestMapping(value = "/system/deleteUser", method = RequestMethod.POST)
	public DataResponse<Void> updateUser(@RequestBody UserDeleteReq req){
		userService.deleteUser(req);
		return DataResponse.success();
	}
	
	
	@ApiOperation("用户列表查询")
	@RequestMapping(value = "/system/queryUserList", method = RequestMethod.POST)
	public DataResponse<List<User>> queryUserList(@RequestBody User req){
		List<User> list = userService.queryUserList(req);
		return DataResponse.success(list);
	}
	
	
	@ApiOperation("查询用户资源")
	@RequestMapping(value = "/system/getUserResource", method = RequestMethod.POST)
	public DataResponse<List<UserResource>> getUserResource(@RequestParam(value = "userCode", required = true) String userCode){
		return DataResponse.success(userService.getUserResource(userCode));
	}
	
}
