package com.wql.cloud.systemservice.controller.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.systemservice.service.role.RoleService;

@RestController
public class RoleController {

	@Autowired
	private RoleService roleService;
	
}
