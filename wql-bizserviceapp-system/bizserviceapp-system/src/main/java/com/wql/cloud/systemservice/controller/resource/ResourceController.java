package com.wql.cloud.systemservice.controller.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.systemservice.service.resource.ResourceService;

@RestController
public class ResourceController {

	@Autowired
	private ResourceService resourceService;
	
	
}
