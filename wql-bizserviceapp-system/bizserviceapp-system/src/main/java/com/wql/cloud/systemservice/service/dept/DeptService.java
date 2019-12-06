package com.wql.cloud.systemservice.service.dept;

import java.util.List;

import com.wql.cloud.systemservice.pojo.domain.dept.Dept;

public interface DeptService {

	/**
	 * 查询所有部门
	 * @return
	 */
	List<Dept> getDeptList();

}
