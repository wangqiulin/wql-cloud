package com.wql.cloud.userservice.pojo.domain;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

import cn.afterturn.easypoi.excel.annotation.Excel;

@Table(name = "t_user")
public class User extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Excel(name = "姓名", orderNum = "0", width = 15.0)
	private String userName;

	@Excel(name = "密码", orderNum = "1", width = 30.0)
	private String userPwd;

	static class UserBuilder {
		private String userName;
		private String userPwd;

		public UserBuilder setUserName(String userName) {
			this.userName = userName;
			return this;
		}

		public UserBuilder setUserPwd(String userPwd) {
			this.userPwd = userPwd;
			return this;
		}

		public String getUserName() {
			return userName;
		}

		public String getUserPwd() {
			return userPwd;
		}

		public User builder(){
			return new User(this);
		}
	}

	public User(UserBuilder builder) {
		this.userName = builder.getUserName();
		this.userPwd = builder.getUserPwd();
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public static void main(String[] args) {
		new User.UserBuilder().setUserName("你好").builder();
		new User.UserBuilder()
				.setUserName("你好")
				.setUserPwd("123")
				.builder();
	}

}
