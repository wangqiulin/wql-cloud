package com.wql.cloud.systemservice.pojo.res.user;

import java.io.Serializable;

/**
 * 用户资源
 * 
 * @author wangqiulin
 *
 */
public class UserResource implements Serializable {

	private static final long serialVersionUID = 1L;

	/**资源明细名称*/
	private String resourceDetailName;

	/**资源明细url*/
	private String resourceDetailUrl;

	public String getResourceDetailName() {
		return resourceDetailName;
	}

	public void setResourceDetailName(String resourceDetailName) {
		this.resourceDetailName = resourceDetailName;
	}

	public String getResourceDetailUrl() {
		return resourceDetailUrl;
	}

	public void setResourceDetailUrl(String resourceDetailUrl) {
		this.resourceDetailUrl = resourceDetailUrl;
	}
	
}
