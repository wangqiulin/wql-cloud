package com.wql.cloud.systemservice.controller.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.systemservice.pojo.req.menu.UserMenuReq;
import com.wql.cloud.systemservice.pojo.res.menu.UserMenuRes;
import com.wql.cloud.systemservice.service.menu.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "菜单接口")
@RestController
public class MenuController {

	@Autowired
	private MenuService menuService;
	
	@ApiOperation("查询用户菜单")
	@RequestMapping(value = "/system/menu/getUserMenu", method = RequestMethod.POST)
	public DataResponse<UserMenuRes> getUserMenu(@RequestBody UserMenuReq req){
		return DataResponse.success(menuService.getUserMenu(req));
	}
	
}
