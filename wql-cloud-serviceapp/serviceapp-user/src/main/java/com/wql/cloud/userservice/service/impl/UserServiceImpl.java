package com.wql.cloud.userservice.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wql.cloud.basic.datasource.commonService.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.basic.excel.xxl.ExcelUtil;
import com.wql.cloud.basic.redisson.distributeLock.aop.DistributedLock;
import com.wql.cloud.basic.response.constant.BusinessEnum;
import com.wql.cloud.basic.response.constant.DataResponse;
import com.wql.cloud.userservice.domain.User;
import com.wql.cloud.userservice.excel.UserExcel;
import com.wql.cloud.userservice.service.UserService;

/**
 * 
 *  @Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    @LoadBalanced
    private RestTemplate balancedRestTemplate;
 * 
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

	@Override
	@TargetDataSource(name = "read")
	@Cacheable(cacheNames="users")
	public DataResponse queryUserAll(String filePath) {
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		List<User> list = this.queryList();
		
		List<UserExcel> userExcelList = new ArrayList<>();
		list.stream().forEach(user -> {
			UserExcel userExcel = new UserExcel();
			try {
				BeanUtils.copyProperties(userExcel, user);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			userExcelList.add(userExcel);
		});
		
		ExcelUtil.exportToFile(filePath, userExcelList);
		
		dr.setData(list);
		return dr;
	}

	
	@Override
	@TargetDataSource(name = "read")
	public DataResponse queryUserById(Integer id) {
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		dr.setData(this.queryById(id));
		return dr;
	}

	
	@Override
	@DistributedLock(lockName="lock:test", tryLock=true)
	public DataResponse updateUserById(Integer id) {
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		User record = new User();
		record.setId(id);
		record.setUserName("王秋林");
		int flag = this.updateSelective(record);
		dr.setData(flag);
		return dr;
	}

	
	@Override
	public DataResponse deleteUserById(Integer id) {
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		int flag = this.deleteById(id);
		dr.setData(flag);
		return dr;
	}

}
