package com.wql.cloud.serviceapp.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wql.cloud.serviceapp.user.domain.User;
import com.wql.cloud.serviceapp.user.mapper.UserMapper;
import com.wql.cloud.serviceapp.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public List<User> queryAll() {
		List<User> list = userMapper.selectAll();
		return list;
	}


}
