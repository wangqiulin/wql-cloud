package com.wql.cloud.basic.datasource.tk.special;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

/**
 * 自定义mapper方法，主要是解决批量插入/更新问题(主键字段也插入)
 * 
 * @author wangqiulin
 * @param <T>
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface SpecialBatchMapper<T> {

	/**
	 * 批量插入数据库，所有字段都插入，包括主键
	 *
	 * @return
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@InsertProvider(type = SpecialBatchProvider.class, method = "batchInsertList")
	int batchInsertList(List<T> list);
	
}
