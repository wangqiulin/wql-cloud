package com.wql.cloud.userservice.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.baseservice.BaseService;
import com.wql.cloud.basic.datasource.dynamic.TargetDataSource;
import com.wql.cloud.tool.jwt.CheckResult;
import com.wql.cloud.tool.jwt.JwtUtil;
import com.wql.cloud.userservice.pojo.domain.User;
import com.wql.cloud.userservice.service.UserService;

import io.jsonwebtoken.Claims;


/**
 * 分布式锁： @DistributedLock(lockName="lock:test", tryLock=true)
 * 
 * @author wangqiulin
 *
 */
@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

	public final Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
	@Override
	@Transactional
	public String register(User req) {
		User record = new User();
		record.setUserName(req.getUserName());
		User user = getByRecord(record);
		Assert.isNull(user, "用户名已存在");
		//保存
		record.setUserPwd(req.getUserPwd());
		saveSelective(record);
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
		return this.updateSelectiveById(req);
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
		String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzIiwic3ViIjoi546L56eL5p6XIiwiaXNzIjoidXNlciIsImlhdCI6MTU2NTI1MDc3MSwiZXhwIjoxNTY1MzM3MTcxfQ.yfCoqt1EVGURYilIU5YsOmAeuNimUrZMpAQafN7809Q";
		CheckResult checkResult = JwtUtil.validateJWT(token);
		if(checkResult.getSuccess()) {
			Claims claims = checkResult.getClaims();
			String id2 = claims.getId();
			String subject = claims.getSubject();
			Date expiration = claims.getExpiration();
			Date issuedAt = claims.getIssuedAt();
			String issuer = claims.getIssuer();
			System.out.println("==============");
			
		} else {
			
		}
		User user = this.getById(req.getId());
		return user;
	}
	
	@Override
	@TargetDataSource(name = "read")
	public List<User> queryList(User req) {
		List<User> list = this.listByRecord(req);
		return list;
	}

	@Override
	@TargetDataSource(name = "read")
	public PageInfo<User> queryPageList(User req) {
		return this.pageListByRecord(req.getPage(), req.getPageSize(), req);
	}

}
