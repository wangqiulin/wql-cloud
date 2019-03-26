package com.wql.cloud.basic.datasource.baseservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * 单表通用的service层的抽象类
 */
public abstract class BaseService<T> {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String ORDER_BYID_DESC = "id desc";
    
    @Autowired
    private Mapper<T> mapper;
    
    //=================查询一条===================//
    
    public T getById(Integer id) {
        return this.mapper.selectByPrimaryKey(id);
    }
    
    public T getById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }
    
    public T getByRecord(T record) {
        return this.mapper.selectOne(record);
    }

    public T getByExample(Example example) {
        return this.mapper.selectOneByExample(example);
    }
    
    //=================查询多条===================//
    
    public List<T> list() {
        return this.mapper.selectAll();
    }

    public List<T> listByRecord(T record) {
        return this.mapper.select(record);
    }

    public List<T> listByExample(Example example) {
        return this.mapper.selectByExample(example);
    }

    //=================分页查询(查询总数)===================//
    
    /**
     * 条件查询
     * @param page 页数
     * @param rows 条数
     * @param record
     * @return
     */
    public PageInfo<T> pageListByRecord(Integer page, Integer rows, T record) {
    	page = (page == null || page <= 0) ? 1 : page;
    	rows = (rows == null || rows <= 0) ? 10 : rows;
    	return pageListByRecord(page, rows, record, false);
    }
    
    /**
     * 条件查询
     * @param page
     * @param rows
     * @param record
     * @param desc  是否倒序 （true：按id倒序, false：按id升序）
     * @return
     */
    public PageInfo<T> pageListByRecord(Integer page, Integer rows, T record, boolean desc) {
    	page = (page == null || page <= 0) ? 1 : page;
    	rows = (rows == null || rows <= 0) ? 10 : rows;
    	//分页，根据默认配置
    	PageHelper.startPage(page, rows); 
        if(desc) {
        	PageHelper.orderBy(ORDER_BYID_DESC);
        }
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    /**
     * 条件查询
     * @param page
     * @param rows
     * @param example
     * @return
     */
    public PageInfo<T> pageListByExample(Integer page, Integer rows, Example example) {
    	page = (page == null || page <= 0) ? 1 : page;
    	rows = (rows == null || rows <= 0) ? 10 : rows;
    	return pageListByExample(page, rows, example, false);
    }
    
    /**
     * 条件查询
     * @param page
     * @param rows
     * @param example
     * @param desc 是否倒序 （true倒序， false升序）
     * @return
     */
    public PageInfo<T> pageListByExample(Integer page, Integer rows, Example example, boolean desc) {
    	page = (page == null || page <= 0) ? 1 : page;
    	rows = (rows == null || rows <= 0) ? 10 : rows;
        PageHelper.startPage(page, rows);
        if(desc) {
        	PageHelper.orderBy(ORDER_BYID_DESC);
        }
        return new PageInfo<T>(this.mapper.selectByExample(example));
    }

    
    //=================分页查询(不查总数)===================//
    
    /**
     * 条件查询(不查总数)
     * @param page 页数
     * @param rows 条数
     * @param record
     * @return
     */
    public PageInfo<T> pageListByRecordWithoutCount(Integer page, Integer rows, T record) {
    	page = (page == null || page <= 0) ? 1 : page;
    	rows = (rows == null || rows <= 0) ? 10 : rows;
    	return pageListByRecord(page, rows, record, false);
    }
    
    /**
     * 条件查询(不查总数)
     * @param page
     * @param rows
     * @param record
     * @param desc  是否倒序 （true：按id倒序, false：按id升序）
     * @return
     */
    public PageInfo<T> pageListByRecordWithoutCount(Integer page, Integer rows, T record, boolean desc) {
    	page = (page == null || page <= 0) ? 1 : page;
    	rows = (rows == null || rows <= 0) ? 10 : rows;
    	//分页，根据默认配置
    	PageHelper.startPage(page, rows, false);  //false表示不进行count查询
        if(desc) {
        	PageHelper.orderBy(ORDER_BYID_DESC);
        }
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    /**
     * 条件查询(不查总数)
     * @param page
     * @param rows
     * @param example
     * @return
     */
    public PageInfo<T> queryPageListByExampleWithoutCount(Integer page, Integer rows, Example example) {
    	page = (page == null || page <= 0) ? 1 : page;
    	rows = (rows == null || rows <= 0) ? 10 : rows;
    	return pageListByExample(page, rows, example, false);
    }
    
    /**
     * 条件查询(不查总数)
     * @param page
     * @param rows
     * @param example
     * @param desc 是否倒序 （true倒序， false升序）
     * @return
     */
    public PageInfo<T> pageListByExampleWithoutCount(Integer page, Integer rows, Example example, boolean desc) {
    	page = (page == null || page <= 0) ? 1 : page;
    	rows = (rows == null || rows <= 0) ? 10 : rows;
        PageHelper.startPage(page, rows, false);   //false表示不进行count查询
        if(desc) {
        	PageHelper.orderBy(ORDER_BYID_DESC);
        }
        return new PageInfo<T>(this.mapper.selectByExample(example));
    }
    
    
    //=================查询数量===================//	
    
    public int countByRecord(T record) {
        return this.mapper.selectCount(record);
    }

    public int countByExample(Example example) {
        return this.mapper.selectCountByExample(example);
    }
    
    //=================根据主键，查询记录是否存在===================//	
    
    public boolean existsById(Integer id){
    	return this.mapper.existsWithPrimaryKey(id);
    }
    
    public boolean existsById(Long id){
    	return this.mapper.existsWithPrimaryKey(id);
    }
    
    //=================新增===================//
    
    public int save(T record) {
        return this.mapper.insert(record);
    }

    public int saveSelective(T record) {
        return this.mapper.insertSelective(record);
    }
    
    //=================更新===================//
    
    public int updateById(T record) {
        return this.mapper.updateByPrimaryKey(record);
    }

    public int updateSelectiveById(T record) {
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    public int updateByExample(T record, Example example) {
        return this.mapper.updateByExample(record, example);
    }

    public int updateByExampleSelective(T record, Example example) {
        return this.mapper.updateByExampleSelective(record, example);
    }

    
    //=================删除===================//
    
    public int removeById(Integer id) {
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    public int removeById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    public int removeByRecord(T record) {
        return this.mapper.delete(record);
    }
    
    public int removeByExample(Example example) {
        return this.mapper.deleteByExample(example);
    }
    
    public int removeByIds(List<?> ids, Class<T> clazz) {
    	Example example = new Example(clazz);
    	example.createCriteria().andIn("id", ids);
        return this.mapper.deleteByExample(example);
    }
    
}
