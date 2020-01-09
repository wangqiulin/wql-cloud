package com.wql.cloud.systemservice.pojo.res.menu;

import java.io.Serializable;
import java.util.List;

public class UserMenuRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String menuCode;
	
	private String menuName;
	
	private String parentMenuCode;
	
	private Integer menuSort;
	
	private String resourceCode;
	
	/**路径url*/
	private String path;
	
	private String component;
	
	/**子菜单*/
	private List<UserMenuRes> children;

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentMenuCode() {
		return parentMenuCode;
	}

	public void setParentMenuCode(String parentMenuCode) {
		this.parentMenuCode = parentMenuCode;
	}

	public Integer getMenuSort() {
		return menuSort;
	}

	public void setMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public List<UserMenuRes> getChildren() {
		return children;
	}

	public void setChildren(List<UserMenuRes> children) {
		this.children = children;
	}
	
}
