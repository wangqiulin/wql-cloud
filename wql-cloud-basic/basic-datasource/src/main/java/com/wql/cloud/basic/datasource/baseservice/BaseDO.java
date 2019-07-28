package com.wql.cloud.basic.datasource.baseservice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @Transient 非实体类字段
 * @JsonIgnore 请求和返回时都忽略该字段
 * @JsonIgnoreProperties({ "字段1", "字段2" }) 请求时不忽略字段，返回时忽略该字段
 * 
 * @author wangqiulin
 *
 */
public abstract class BaseDO implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
	private Date createDate;

	@JsonFormat(pattern = DATE_FORMAT, timezone = "GMT+8")
	private Date updateDate;

	//版本号，用于乐观锁
	@JsonIgnore
	private Long version;  
	
	//数据标志: 0无效, 1有效
	@JsonIgnore
	private Integer dataFlag;  
	
	@Transient
	public Integer page;

	@Transient
	public Integer pageSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Integer getDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(Integer dataFlag) {
		this.dataFlag = dataFlag;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
