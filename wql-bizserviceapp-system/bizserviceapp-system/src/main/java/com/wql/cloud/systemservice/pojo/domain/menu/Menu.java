package com.wql.cloud.systemservice.pojo.domain.menu;

import java.io.Serializable;

import javax.persistence.Table;

import com.wql.cloud.basic.datasource.baseservice.BaseDO;

@Table(name = "t_system_menu")
public class Menu extends BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String menuCode;

	private String menuName;

	private String menuParentCode;
	
	private Integer menuSort;
	
	private Integer menuShow;

	private String menuResourceCode;
	
	private String menuIcon;

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

	public String getMenuParentCode() {
		return menuParentCode;
	}

	public void setMenuParentCode(String menuParentCode) {
		this.menuParentCode = menuParentCode;
	}

	public Integer getMenuSort() {
		return menuSort;
	}

	public void setMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
	}

	public Integer getMenuShow() {
		return menuShow;
	}

	public void setMenuShow(Integer menuShow) {
		this.menuShow = menuShow;
	}

	public String getMenuResourceCode() {
		return menuResourceCode;
	}

	public void setMenuResourceCode(String menuResourceCode) {
		this.menuResourceCode = menuResourceCode;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
	
}
