package com.wql.cloud.systemservice.controller.dept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.systemservice.service.dept.DeptService;

@RestController
public class DeptController {

	@Autowired
	private DeptService deptService;
	
}
