package com.wql.cloud.userservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.tool.bean.BeanUtils;
import com.wql.cloud.tool.excel.csv.CsvDownloadUtil;
import com.wql.cloud.userservice.pojo.domain.User;
import com.wql.cloud.userservice.pojo.res.UserExcel;
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
	public DataResponse<Void> save(@RequestBody User req) {
		return userService.save(req) == 1 ? DataResponse.success() : DataResponse.failure();
	}
	
	
	@ApiOperation(value = "修改")
	@PostMapping("/user/update")
	public DataResponse<Void> update(@RequestBody User req) {
		return userService.update(req) > 0 ? DataResponse.success() : DataResponse.failure();
	}
	
	
	@ApiOperation(value = "删除")
	@PostMapping("/user/delete")
	public DataResponse<Void> delete(@RequestBody User req) {
		return userService.delete(req) > 0 ? DataResponse.success() : DataResponse.failure();
	}
	
	
	@ApiOperation(value = "查询列表")
	@PostMapping("/user/queryList")
	public DataResponse<List<User>> queryList(@RequestBody User req) {
		return DataResponse.success(userService.queryList(req));
	}
	
	
	@ApiOperation(value = "分页查询列表")
	@PostMapping("/user/queryPageList")
	public DataResponse<PageInfo<User>> queryPageList(@RequestBody User req) {
		return DataResponse.success(userService.queryPageList(req));
	}
	
	
	@ApiOperation(value = "查询记录")
	@PostMapping("/user/query")
	public DataResponse<User> query(@RequestBody User req) {
		return DataResponse.success(userService.query(req));
	}
	
	
	@ApiOperation(value = "导出")
	@GetMapping(value = "/user/export")
    public void export(User req) throws Exception {
		List<Map<String, Object>> userExcelList = new ArrayList<>();
		//查询
    	List<User> list = userService.queryList(req);
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
	    Map<String, String> csvHeader = CsvDownloadUtil.getCsvHeader(UserExcel.class);
		CsvDownloadUtil.writeHeader(csvHeader, sheetNamePrefix, response);
		CsvDownloadUtil.writeData(csvHeader, userExcelList, response);
    	response.getOutputStream().close();
    }
	
}
