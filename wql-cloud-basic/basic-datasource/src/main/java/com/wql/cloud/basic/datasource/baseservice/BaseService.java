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

/**
 * 单表通用的service层的抽象类
 */
public abstract class BaseService<T extends BaseDO> {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final Integer PAGE = 1;
    public static final Integer ROWS = 10;
    public static final String ID = "id";
    
    @Autowired
    private MyMapper<T> mapper;
    
    //=================查询一条===================//
    
    public T getById(Integer id) {
    	Assert.notNull(id, "id不能为空");
        return this.mapper.selectByPrimaryKey(id);
    }
    
    public T getById(Long id) {
    	Assert.notNull(id, "id不能为空");
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
    
    public PageInfo<T> pageListByRecord(Integer page, Integer rows, T record) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	PageHelper.startPage(page, rows); 
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    public PageInfo<T> pageListByExample(Integer page, Integer rows, Example example) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
        PageHelper.startPage(page, rows);
        return new PageInfo<T>(this.mapper.selectByExample(example));
    }

    
    //=================分页查询(不查总数)===================//
    
    public PageInfo<T> pageListByRecordWithoutCount(Integer page, Integer rows, T record) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	//false表示不进行count查询
    	PageHelper.startPage(page, rows, false);  
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    public PageInfo<T> pageListByExampleWithoutCount(Integer page, Integer rows, Example example) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	//false表示不进行count查询
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
    	Assert.notNull(id, "id不能为空");
    	return this.mapper.existsWithPrimaryKey(id);
    }
    
    public boolean existsById(Long id){
    	Assert.notNull(id, "id不能为空");
    	return this.mapper.existsWithPrimaryKey(id);
    }
    
    //=================新增===================//
    
    public Integer saveSelective(T record) {
    	record.setId(null);
        return this.mapper.insertSelective(record);
    }
    
    public Integer insertList(List<T> recordList) {
    	for (T record : recordList) {
    		record.setId(null);
		}
        return this.mapper.insertList(recordList);
    }
    
    //=================更新===================//
    
    public Integer updateSelectiveById(T record) {
    	record.setUpdateDate(new Date());
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    public Integer updateSelectiveByExample(T record, Example example) {
    	record.setUpdateDate(new Date());
        return this.mapper.updateByExampleSelective(record, example);
    }

    /**
     * 数据更新，存在乐观锁控制
     * @param record
     * @param example
     * @return
     */
    public Integer updateSelectiveByVersion(T record, Example example) {
    	Assert.notNull(record.getVersion(), "版本号不能为空");
    	//版本号作为条件
    	example.and().andEqualTo("version", record.getVersion());
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
    public int updateBatchByPrimaryKeySelective(List<T> recordList) {
    	Assert.notEmpty(recordList, "修改内容不能为空");
    	return this.mapper.updateBatchByPrimaryKeySelective(recordList);
    }
    
    
    //=================删除===================//
    
    public Integer removeById(Integer id) {
    	Assert.notNull(id, "id不能为空");
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    public Integer removeById(Long id) {
    	Assert.notNull(id, "id不能为空");
        return this.mapper.deleteByPrimaryKey(id);
    }

    public Integer removeByRecord(T record) {
        return this.mapper.delete(record);
    }
    
    public Integer removeByExample(Example example) {
        return this.mapper.deleteByExample(example);
    }
    
    public Integer removeByIds(List<?> ids, Class<T> clazz) {
    	Assert.notEmpty(ids, "id不能为空");
    	Example example = new Example(clazz);
    	example.createCriteria().andIn(ID, ids);
        return this.mapper.deleteByExample(example);
    }
    
}
