package com.wql.cloud.userservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.wql.cloud.basic.excel.csv.CsvDownloadUtil;
import com.wql.cloud.basic.response.constant.DataResponse;
import com.wql.cloud.userservice.domain.User;
import com.wql.cloud.userservice.excel.UserExcel;
import com.wql.cloud.userservice.mapper.UserMapper;
import com.wql.cloud.userservice.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/user/query/all")
	public DataResponse queryUserAll(String filePath) {
		return userService.queryUserAll(filePath);
	}
	
	@PostMapping("/user/query/{id}")
	public DataResponse queryUserById(@PathVariable("id") Integer id) {
		return userService.queryUserById(id);
	}
	
	@PostMapping("/user/update/{id}")
	public DataResponse updateUserById(@PathVariable("id") Integer id) {
		return userService.updateUserById(id);
	}
	
	@PostMapping("/user/delete/{id}")
	public DataResponse deleteUserById(@PathVariable("id") Integer id) {
		return userService.deleteUserById(id);
	}
	
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		String forObject = restTemplate.getForObject("http://localhost:8888/wql-boot/test2", String.class);
		System.out.println(forObject);
	}
	
	
	
	
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping(value = "/user/export", method = RequestMethod.GET)
    public void export() {
        try {
        	getWorkbook();
        } catch (Exception e) {
        }
    }
	
	
	@Autowired
	private UserMapper userMapper;
	
	//==============================================================//
	
	public boolean getWorkbook() throws Exception {
		List<Map<String, Object>> userExcelList = new ArrayList<>();
		
        List<User> list = userMapper.selectAll();
		list.stream().forEach(user -> {
			UserExcel userExcel = new UserExcel();
			try {
				BeanUtils.copyProperties(userExcel, user);
				userExcelList.add(CsvDownloadUtil.bean2map(userExcel, UserExcel.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        
		String sheetNamePrefix = "用户列表2";
	    Map<String, String> csvHeader = CsvDownloadUtil.getCSVHeader(UserExcel.class);
		CsvDownloadUtil.writeHeader(csvHeader, sheetNamePrefix, response);
		CsvDownloadUtil.writeData(csvHeader, userExcelList, response);
		response.getWriter().close();
		return true;
    }
	
}
