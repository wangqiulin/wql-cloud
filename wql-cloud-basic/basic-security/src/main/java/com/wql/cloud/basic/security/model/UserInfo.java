package com.wql.cloud.basic.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 登录用户信息（包含权限）
 */
public class UserInfo extends UserSession implements UserDetails  {
	
	private static final long serialVersionUID = -8275309778224187755L;
	
    private Collection<? extends GrantedAuthority> authorities;
   
    public UserInfo(Collection<? extends GrantedAuthority> authorities) {
    	this.authorities = authorities;
    }
   
    /**
     * 权限信息
     */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * 用户密码
	 */
	@JsonIgnore
	@Override
	public String getPassword() {
		return null;
	}	
	
	/**
	 * 用户名
	 */
	@Override
	public String getUsername() {
		return getUserName();
	}

    public void setUsername(String userName) {
	    super.setUserName(userName);
    }

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public UserInfo() {
	    super();
    }
}
