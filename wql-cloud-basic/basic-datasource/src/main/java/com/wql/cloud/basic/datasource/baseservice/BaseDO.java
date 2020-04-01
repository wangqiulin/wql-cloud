package com.wql.cloud.basic.datasource.baseservice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 每个表的基类
 * 
 * @JsonIgnore: 请求和返回时都忽略该字段
 * @JsonFormat: 后台到前台的时间格式的转换
 * 
 * @author wangqiulin
 *
 */
@ApiModel("数据库表对应实体类的基类")
public abstract class BaseDO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	@ApiModelProperty("主键自增")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty("数据创建时间")
	@DateTimeFormat(pattern = YMDHMS)
	@JsonFormat(pattern = YMDHMS, timezone = "GMT+8")
	private Date createDate;

	@ApiModelProperty("数据更新时间")
	@DateTimeFormat(pattern = YMDHMS)
	@JsonFormat(pattern = YMDHMS, timezone = "GMT+8")
	private Date updateDate;

	@ApiModelProperty("版本号，用于乐观锁")
	@JsonIgnore
	private Long version;  
	
	@ApiModelProperty("数据标志: 0无效, 1有效")
	@JsonIgnore
	private Integer dataFlag;  
	
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

}
