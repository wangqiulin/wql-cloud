package com.wql.cloud.systemservice.controller.dept;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.systemservice.pojo.domain.dept.Dept;
import com.wql.cloud.systemservice.service.dept.DeptService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("部门")
@RestController
public class DeptController {

	@Autowired
	private DeptService deptService;
	
	@ApiOperation("查询部门列表")
	@RequestMapping(value = "/system/getDeptList", method = RequestMethod.POST)
	public DataResponse<List<Dept>> getDeptList(){
		return DataResponse.success(deptService.getDeptList());
	}
	
}
