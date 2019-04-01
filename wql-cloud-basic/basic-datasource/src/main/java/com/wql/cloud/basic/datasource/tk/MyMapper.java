package com.wql.cloud.basic.datasource.tk;

import com.wql.cloud.basic.datasource.tk.special.SpecialBatchMapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 
 * @author wangqiulin
 *
 * @param <T>
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T>, SpecialBatchMapper<T> {
    
}
