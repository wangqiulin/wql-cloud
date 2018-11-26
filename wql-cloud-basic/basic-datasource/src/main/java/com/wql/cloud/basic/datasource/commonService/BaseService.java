package com.wql.cloud.basic.datasource.commonService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * 单表通用的service层的抽象类
 */
public abstract class BaseService<T> {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Mapper<T> mapper;
    
    //=================查询===================//
    
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }
    
    public T queryById(Integer id) {
        return this.mapper.selectByPrimaryKey(id);
    }
    
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    public T queryOne(Example example) {
        List<T> list = this.mapper.selectByExample(example);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
    
    public List<T> queryList() {
        return this.mapper.select(null);
    }

    public List<T> queryListByCondition(T record) {
        return this.mapper.select(record);
    }

    public List<T> queryListByExample(Example example) {
        return this.mapper.selectByExample(example);
    }

    //=================分页查询===================//
    
    public PageInfo<T> queryPageListByCondition(Integer page, Integer rows, T record) {
        PageHelper.startPage(page, rows);
        PageHelper.orderBy("id desc");
        List<T> list = this.mapper.select(record);
        return new PageInfo<T>(list);
    }
    
    public PageInfo<T> queryPageListByExample(Integer page, Integer rows, Example example) {
        PageHelper.startPage(page, rows);
        PageHelper.orderBy("id desc");
        List<T> list = this.mapper.selectByExample(example);
        return new PageInfo<T>(list);
    }

    //=================查询数量===================//	
    
    public int queryCountByCondition(T record) {
        return this.mapper.selectCount(record);
    }

    public int queryCountByExample(Example example) {
        return this.mapper.selectCountByExample(example);
    }
    
    //=================新增===================//
    
    public int save(T record) {
        return this.mapper.insert(record);
    }

    public int saveSelective(T record) {
        return this.mapper.insertSelective(record);
    }
    
    //=================更新===================//
    
    public int update(T record) {
        return this.mapper.updateByPrimaryKey(record);
    }

    public int updateSelective(T record) {
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    public int updateByExample(T record, Example example) {
        return this.mapper.updateByExample(record, example);
    }

    public int updateByExampleSelective(T record, Example example) {
        return this.mapper.updateByExampleSelective(record, example);
    }

    
    //=================删除===================//
    
    public int deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    public int deleteById(Integer id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    public int deleteByCondition(T record) {
        return this.mapper.delete(record);
    }

    public int deleteByIds(Class<T> clazz, List<Integer> ids) {
    	Example example = new Example(clazz);
    	example.createCriteria().andIn("id", ids);
        return this.mapper.deleteByExample(example);
    }
    
    
}
