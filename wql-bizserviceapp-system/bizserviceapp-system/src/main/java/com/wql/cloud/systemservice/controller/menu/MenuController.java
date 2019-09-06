package com.wql.cloud.systemservice.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.systemservice.service.memu.MenuService;

@RestController
public class MenuController {

	@Autowired
	private MenuService menuService;
	
}
