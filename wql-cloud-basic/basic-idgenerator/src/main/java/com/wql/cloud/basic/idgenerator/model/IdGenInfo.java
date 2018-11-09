package com.wql.cloud.basic.idgenerator.model;

import java.util.Date;

/**
 * 发号器基础类
 */
public class IdGenInfo {
	private long id;
	private long lastIssued;
	private String projectName;
	private String modelName;
	private int steps;
	private Date createAt;
	private Date updateAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLastIssued() {
		return lastIssued;
	}

	public void setLastIssued(long lastIssued) {
		this.lastIssued = lastIssued;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	@Override
	public String toString() {
		return "IdGenInfo [id=" + id + ", lastIssued=" + lastIssued + ", projectName=" + projectName + ", modelName="
				+ modelName + ", steps=" + steps + ", createAt=" + createAt + ", updateAt=" + updateAt + "]";
	}
	
}
