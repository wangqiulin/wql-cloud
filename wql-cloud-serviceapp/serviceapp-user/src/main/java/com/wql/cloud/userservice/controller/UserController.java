package com.wql.cloud.userservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wql.cloud.basic.excel.csv.CsvDownloadUtil;
import com.wql.cloud.basic.response.constant.BusinessEnum;
import com.wql.cloud.basic.response.constant.DataResponse;
import com.wql.cloud.tool.bean.BeanUtils;
import com.wql.cloud.userservice.domain.User;
import com.wql.cloud.userservice.excel.UserExcel;
import com.wql.cloud.userservice.model.req.UserReq;
import com.wql.cloud.userservice.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "用户")
@RestController
public class UserController {

	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "新增")
	@PostMapping("/user/save")
	public DataResponse saveUser(@RequestBody UserReq req) {
		return userService.saveUser(req);
	}
	
	@ApiOperation(value = "查询列表")
	@PostMapping("/user/query/all")
	public DataResponse queryUserAll() {
		return userService.queryUserAll();
	}
	
	@ApiOperation(value = "根据id，查询记录")
	@PostMapping("/user/query/{id}")
	public DataResponse queryUserById(@PathVariable("id") Integer id) {
		return userService.queryUserById(id);
	}
	
	@ApiOperation(value = "根据id，修改记录")
	@PostMapping("/user/update/{id}")
	public DataResponse updateUserById(@PathVariable("id") Integer id) {
		return userService.updateUserById(id);
	}
	
	@ApiOperation(value = "根据id，删除记录")
	@PostMapping("/user/delete/{id}")
	public DataResponse deleteUserById(@PathVariable("id") Integer id) {
		return userService.deleteUserById(id);
	}
	
	@ApiOperation(value = "导出")
	@GetMapping(value = "/user/export")
    public void export() throws Exception {
		List<Map<String, Object>> userExcelList = new ArrayList<>();
		//查询
    	DataResponse dr = userService.queryUserAll();
    	if(BusinessEnum.SUCCESS.getCode() == dr.getCode()) {
    		//转换数据
    		@SuppressWarnings("unchecked")
			List<User> list = (List<User>) dr.getData();
    		list.stream().forEach(user -> {
				try {
					UserExcel userExcel = new UserExcel();
	    			BeanUtils.copy(user, userExcel);
					userExcelList.add(CsvDownloadUtil.bean2map(userExcel, UserExcel.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
    		});
    		String sheetNamePrefix = "用户列表";
    	    Map<String, String> csvHeader = CsvDownloadUtil.getCSVHeader(UserExcel.class);
    		CsvDownloadUtil.writeHeader(csvHeader, sheetNamePrefix, response);
    		CsvDownloadUtil.writeData(csvHeader, userExcelList, response);
    	}
    	response.getOutputStream().close();
    }
	
}
