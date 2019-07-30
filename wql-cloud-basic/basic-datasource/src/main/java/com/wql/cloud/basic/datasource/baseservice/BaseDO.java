package com.wql.cloud.basic.datasource.baseservice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @Transient 非实体类字段
 * @JsonIgnore 请求和返回时都忽略该字段
 * @JsonIgnoreProperties({ "字段1", "字段2" }) 请求时不忽略字段，返回时忽略该字段
 * 
 * jackson实体转json时 为NULL不参加序列化的汇总： https://www.cnblogs.com/weiapro/archive/2017/10/11/7653443.html
 * 
 * @author wangqiulin
 *
 */
public abstract class BaseDO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 主键自增
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = YYYYMMDDHHMMSS, timezone = "GMT+8")
	private Date createDate;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = YYYYMMDDHHMMSS, timezone = "GMT+8")
	private Date updateDate;

	/**
	 * 版本号，用于乐观锁
	 */
	@JsonIgnore
	private Long version;  
	
	/**
	 * 数据标志: 0无效, 1有效
	 */
	@JsonIgnore
	private Integer dataFlag;  
	
	/**
	 * 分页参数-页码，默认第1页（非实体类字段，返回时忽略）
	 */
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Integer page;

	/**
	 * 分页参数-每页条数，默认10条（非实体类字段，返回时忽略）
	 */
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Integer pageSize;

	/**
	 * 查询起始时间
	 */
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern = YYYYMMDDHHMMSS)
	@JsonFormat(pattern = YYYYMMDDHHMMSS, timezone = "GMT+8")
	public Date beginTime;
	
	/**
	 * 查询截止时间
	 */
	@Transient
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@DateTimeFormat(pattern = YYYYMMDDHHMMSS)
	@JsonFormat(pattern = YYYYMMDDHHMMSS, timezone = "GMT+8")
	public Date endTime;
	
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

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
