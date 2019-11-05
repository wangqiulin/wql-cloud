package com.wql.cloud.userservice.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.basic.datasource.response.constant.BusinessEnum;
import com.wql.cloud.basic.datasource.response.constant.DataResponse;
import com.wql.cloud.basic.datasource.response.exception.myexp.BusinessException;
import com.wql.cloud.payservice.client.PayClient;
import com.wql.cloud.payservice.pojo.req.CreatePayOrderReq;
import com.wql.cloud.payservice.pojo.res.CreatePayOrderRes;
import com.wql.cloud.tool.jwt.JwtUtil;
import com.wql.cloud.userservice.pojo.domain.User;
import com.wql.cloud.userservice.service.UserService;

import io.seata.spring.annotation.GlobalTransactional;


/**
 * 分布式锁： @DistributedLock(lockName="lock:test", tryLock=true)
 * 
 * @author wangqiulin
 *
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Autowired
	private PayClient payClient;
	
	@Override
//	@Transactional
	@GlobalTransactional(name = "wql-cloud-register-user",rollbackFor = Exception.class) //此注解开启全局事务
//	@DistributedLock(param="userName", tryLock=true)
	public String register(User req) {
		User record = new User();
		record.setUserName(req.getUserName());
		User user = getByRecord(record);
		Assert.isNull(user, "用户名已存在");
		//保存
		record.setUserPwd(req.getUserPwd());
		this.save(record);
		//test
		CreatePayOrderReq createPayOrderReq = new CreatePayOrderReq();
		createPayOrderReq.setAppId("123");
		createPayOrderReq.setBusinessDesc("分布式测试");
		createPayOrderReq.setBusinessType("test");
		DataResponse<CreatePayOrderRes> dataResponse = payClient.createPayOrder(createPayOrderReq);
		if("success".equals(req.getUserName())) {
			logger.info("===============================成功了================"+dataResponse.getCode());
		} else {
			logger.info("===============================失败了================"+dataResponse.getCode());
			throw new BusinessException(BusinessEnum.FAIL);
		}
		//生成token
		String token = JwtUtil.createJWT(String.valueOf(record.getId()), record.getUserName(), 3600*1000);
		return token;
	}
	
	
	@Override
	public String login(User req) {
		//根据用户名查询
		User record = new User();
		record.setUserName(req.getUserName());
		User user = getByRecord(record);
		Assert.notNull(user, "用户不存在");
		Assert.isTrue(StringUtils.equals(req.getUserPwd(), user.getUserPwd()), "密码错误");
		//生成token
		String token = JwtUtil.createJWT(String.valueOf(user.getId()), user.getUserName(), 3600*1000);
		return token;
	}
	
	
	@Override
	public Integer update(User req) {
		return this.updateById(req);
	}

	@Override
	public Integer delete(User req) {
		Assert.notNull(req.getId(), "id为空");
		return this.removeById(req.getId());
	}
	
	@Override
	@TargetDataSource(name = "read")
	public User query(User req) {
		Assert.notNull(req.getId(), "id为空");
		User user = this.getById(req.getId());
		return user;
	}
	
	@Override
	@TargetDataSource(name = "read")
	public List<User> queryList(User req) {
		return this.listByRecord(req);
	}

	@Override
	@TargetDataSource(name = "read")
	public PageInfo<User> queryPageList(User req) {
		return this.pageByRecord(req.getPage(), req.getPageSize(), req);
	}


	@Override
	public String getDynamicTableName() {
		return null;
	}

}
