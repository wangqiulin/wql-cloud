package com.wql.cloud.basic.datasource.baseservice;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wql.cloud.basic.datasource.tk.MyMapper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.IDynamicTableName;

/**
 * 单表通用的service层的抽象类
 */
public abstract class BaseService<T extends BaseDO> implements IDynamicTableName {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final Integer PAGE = 1;
    public static final Integer ROWS = 10;
    public static final String ID = "id";
    public static final String VERSION = "version";
    
    @Autowired
    private MyMapper<T> mapper;
    
    //=================查询一条===================//
    
    public T getById(Integer id) {
    	Assert.notNull(id, "id cann't empty");
        return this.mapper.selectByPrimaryKey(id);
    }
    
    public T getById(Long id) {
    	Assert.notNull(id, "id cann't empty");
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
    
    public PageInfo<T> pageByRecord(Integer page, Integer rows, T record) {
    	Assert.isTrue(page != null && page > 0, "page shouldn't less than 1");
    	Assert.isTrue(rows != null && rows > 0, "rows shouldn't less than 1");
    	PageHelper.startPage(page, rows, true); 
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    public PageInfo<T> pageByExample(Integer page, Integer rows, Example example) {
    	Assert.isTrue(page != null && page > 0, "page shouldn't less than 1");
    	Assert.isTrue(rows != null && rows > 0, "rows shouldn't less than 1");
        PageHelper.startPage(page, rows, true);
        return new PageInfo<T>(this.mapper.selectByExample(example));
    }

    
    //=================分页查询(不查总数)===================//
    
    public PageInfo<T> pageByRecordWithoutCount(Integer page, Integer rows, T record) {
    	Assert.isTrue(page != null && page > 0, "page shouldn't less than 1");
    	Assert.isTrue(rows != null && rows > 0, "rows shouldn't less than 1");
    	PageHelper.startPage(page, rows, false);  
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    public PageInfo<T> pageByExampleWithoutCount(Integer page, Integer rows, Example example) {
    	Assert.isTrue(page != null && page > 0, "page shouldn't less than 1");
    	Assert.isTrue(rows != null && rows > 0, "rows shouldn't less than 1");
        PageHelper.startPage(page, rows, false);  
        return new PageInfo<T>(this.mapper.selectByExample(example));
    }
    
    
    //=================查询数量===================//	
    
    public Integer countByRecord(T record) {
        return this.mapper.selectCount(record);
    }

    public Integer countByExample(Example example) {
        return this.mapper.selectCountByExample(example);
    }
    
    //=================根据主键，查询记录是否存在===================//	
    
    public boolean existsById(Integer id){
    	Assert.notNull(id, "id cann't empty");
    	return this.mapper.existsWithPrimaryKey(id);
    }
    
    public boolean existsById(Long id){
    	Assert.notNull(id, "id cann't empty");
    	return this.mapper.existsWithPrimaryKey(id);
    }
    
    //=================新增===================//
    
    public Integer save(T record) {
    	record.setId(null);
        return this.mapper.insertSelective(record);
    }
    
    public Integer saveList(List<T> recordList) {
    	for (T record : recordList) {
    		record.setId(null);
		}
        return this.mapper.insertList(recordList);
    }
    
    //=================更新===================//
    
    public Integer updateById(T record) {
    	record.setUpdateDate(new Date());
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    public Integer updateByExample(T record, Example example) {
    	record.setUpdateDate(new Date());
        return this.mapper.updateByExampleSelective(record, example);
    }

    /**
     * 数据更新，存在乐观锁控制
     * @param record
     * @param example
     * @return
     */
    public Integer updateByVersion(T record, Example example) {
    	Assert.notNull(record.getVersion(), "version cann't empty");
    	//版本号作为条件
    	example.and().andEqualTo(VERSION, record.getVersion());
    	//通用更新字段
    	record.setUpdateDate(new Date());
    	record.setVersion(record.getVersion() + 1);
        return this.mapper.updateByExampleSelective(record, example);
    }
    
    /**
     * 批量修改
     * @param recordList
     * @return
     */
    public int updateBatchByIds(List<T> recordList) {
    	Assert.notEmpty(recordList, "list cann't empty");
    	return this.mapper.updateBatchByPrimaryKeySelective(recordList);
    }
    
    
    //=================删除===================//
    
    public Integer removeById(Integer id) {
    	Assert.notNull(id, "id cann't empty");
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    public Integer removeById(Long id) {
    	Assert.notNull(id, "id cann't empty");
        return this.mapper.deleteByPrimaryKey(id);
    }

    public Integer removeByRecord(T record) {
        return this.mapper.delete(record);
    }
    
    public Integer removeByExample(Example example) {
        return this.mapper.deleteByExample(example);
    }
    
    public Integer removeByIds(List<?> ids, Class<T> clazz) {
    	Assert.notEmpty(ids, "ids cann't empty");
    	Example example = new Example(clazz);
    	example.createCriteria().andIn(ID, ids);
        return this.mapper.deleteByExample(example);
    }
    
    /**
     * 根据feild字段，删除数据
     * 
     * @param field 删除字段
     * @param list
     * @param clazz
     * @return
     */
    public Integer removeByFeild(String field, List<?> list, Class<T> clazz) {
    	Assert.notEmpty(list, "list cann't empty");
    	Example example = new Example(clazz);
    	example.createCriteria().andIn(field, list);
        return this.mapper.deleteByExample(example);
    }
    
    
}
