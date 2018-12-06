package com.wql.cloud.userservice.excel;

import java.io.Serializable;
import java.util.Date;

import org.apache.poi.hssf.util.HSSFColor;

import com.xuxueli.poi.excel.annotation.ExcelField;
import com.xuxueli.poi.excel.annotation.ExcelSheet;

@ExcelSheet(name = "用户列表", headColor = HSSFColor.HSSFColorPredefined.LIGHT_GREEN)
public class UserExcel implements Serializable {

	private static final long serialVersionUID = 1L;

	@ExcelField(name = "创建时间", dateformat="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	
	@ExcelField(name = "更新时间", dateformat="yyyy-MM-dd HH:mm:ss")
	private Date updateDate;

	@ExcelField(name = "用户名")
	private String userName;
	
	@ExcelField(name = "密码")
	private String userPwd;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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

}
