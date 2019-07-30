package com.wql.cloud.basic.datasource.baseservice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 查询类的基类
 * jackson实体转json时 为NULL不参加序列化的汇总： https://www.cnblogs.com/weiapro/archive/2017/10/11/7653443.html
 * 
 * @Transient：非实体类字段
 * @JsonIgnore：请求和返回时都忽略该字段
 * @JsonInclude：当返回字段的字段内容为null时，忽略该字段
 * @DateTimeFormat： 前后到后台的时间格式的转换
 * 
 * @author wangqiulin
 *
 */
public abstract class BaseQueryDO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	
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
