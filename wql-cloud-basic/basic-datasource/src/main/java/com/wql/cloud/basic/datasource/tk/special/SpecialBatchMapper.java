package com.wql.cloud.basic.datasource.tk.special;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;

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
	 * keyProperty是你POJO实体类与数据库主键映射的那个字段。
	 *
	 * @return
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id") 
	@InsertProvider(type = SpecialBatchProvider.class, method = "batchInsertList")
	int batchInsertList(List<T> list);
	
	
	/**
	 * 根据id批量更新：https://blog.csdn.net/sunct/article/details/90146681
     * 根据Example条件批量更新实体`record`包含的不是null的属性值
     *
     * @return
     */
    @UpdateProvider(type = SpecialBatchProvider.class, method = "updateBatchByPrimaryKeySelective")
    int updateBatchByPrimaryKeySelective(List<? extends T> recordList);
	
}
