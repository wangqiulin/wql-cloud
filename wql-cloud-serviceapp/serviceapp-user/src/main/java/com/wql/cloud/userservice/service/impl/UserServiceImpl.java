package com.wql.cloud.userservice.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.wql.cloud.basic.datasource.commonService.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.basic.redisson.distributeLock.aop.DistributedLock;
import com.wql.cloud.basic.response.constant.BusinessEnum;
import com.wql.cloud.basic.response.constant.DataResponse;
import com.wql.cloud.tool.bean.BeanUtils;
import com.wql.cloud.userservice.domain.User;
import com.wql.cloud.userservice.model.req.UserReq;
import com.wql.cloud.userservice.service.UserService;

/**
 * 
 *  @Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    @LoadBalanced
    private RestTemplate balancedRestTemplate;
 * 
 *  @Cacheable(cacheNames="users")
 * 
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

	@Override
	@Transactional
	public DataResponse saveUser(UserReq req) {
		User record = new User();
		BeanUtils.copy(req, record);
		record.setId(null);
		record.setDataFlag(1);
		record.setCreateDate(new Date());
		record.setUpdateDate(record.getCreateDate());
		Assert.isTrue(this.saveSelective(record) == 1, "新增失败");
		return new DataResponse(BusinessEnum.SUCCESS);
	}
	
	@Override
	@TargetDataSource(name = "read")
	public DataResponse queryUserAll() {
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		dr.setData(this.queryList());
		return dr;
	}

	@Override
	@TargetDataSource(name = "read")
	public DataResponse queryUserById(Integer id) {
		Assert.notNull(id, "id为空");
		DataResponse dr = new DataResponse(BusinessEnum.SUCCESS);
		dr.setData(this.queryById(id));
		return dr;
	}

	@Override
	@DistributedLock(lockName="lock:test", tryLock=true)
	@Transactional
	public DataResponse updateUserById(Integer id) {
		User record = new User();
		record.setId(id);
		record.setUserName("xxx");
		record.setUpdateDate(new Date());
		Assert.isTrue(this.updateSelectiveById(record) == 1, "修改失败");
		return new DataResponse(BusinessEnum.SUCCESS);
	}

	@Override
	public DataResponse deleteUserById(Integer id) {
		Assert.notNull(id, "id为空");
		Assert.isTrue(this.deleteById(id) == 1, "删除失败");
		return new DataResponse(BusinessEnum.SUCCESS);
	}

}
