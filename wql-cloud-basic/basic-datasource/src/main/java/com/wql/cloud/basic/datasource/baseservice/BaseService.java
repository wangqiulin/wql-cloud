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
    public static final String ID_DESC = "id desc";
    
    @Autowired
    private MyMapper<T> mapper;
    
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
     * 分页查询
     * 
     * @param page 页数
     * @param rows 条数
     * @return
     */
    public PageInfo<T> pageList(Integer page, Integer rows) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	return pageListByRecord(page, rows, null, true);
    }
    
    /**
     * 条件分页查询
     * 
     * @param page 页数
     * @param rows 条数
     * @param record
     * @return
     */
    public PageInfo<T> pageListByRecord(Integer page, Integer rows, T record) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	return pageListByRecord(page, rows, record, false);
    }
    
    /**
     * 条件分页查询
     * 
     * @param page
     * @param rows
     * @param record
     * @param desc  是否倒序 （true：按id倒序, false：按id升序）
     * @return
     */
    public PageInfo<T> pageListByRecord(Integer page, Integer rows, T record, boolean desc) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	//分页，根据默认配置
    	PageHelper.startPage(page, rows); 
        if(desc) {
        	PageHelper.orderBy(ID_DESC);
        }
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    /**
     * 条件分页查询
     * 
     * @param page
     * @param rows
     * @param example
     * @return
     */
    public PageInfo<T> pageListByExample(Integer page, Integer rows, Example example) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	return pageListByExample(page, rows, example, false);
    }
    
    /**
     * 条件查询
     * 
     * @param page
     * @param rows
     * @param example
     * @param desc 是否倒序 （true倒序， false升序）
     * @return
     */
    public PageInfo<T> pageListByExample(Integer page, Integer rows, Example example, boolean desc) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
        PageHelper.startPage(page, rows);
        if(desc) {
        	PageHelper.orderBy(ID_DESC);
        }
        return new PageInfo<T>(this.mapper.selectByExample(example));
    }

    
    //=================分页查询(不查总数)===================//
    
    
    /**
     * 条件分页查询(不查总数)
     * 
     * @param page 页数
     * @param rows 条数
     * @param record
     * @return
     */
    public PageInfo<T> pageListByRecordWithoutCount(Integer page, Integer rows, T record) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	return pageListByRecord(page, rows, record, false);
    }
    
    /**
     * 条件分页查询(不查总数)
     * 
     * @param page
     * @param rows
     * @param record
     * @param desc  是否倒序 （true：按id倒序, false：按id升序）
     * @return
     */
    public PageInfo<T> pageListByRecordWithoutCount(Integer page, Integer rows, T record, boolean desc) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	//分页，根据默认配置
    	PageHelper.startPage(page, rows, false);  //false表示不进行count查询
        if(desc) {
        	PageHelper.orderBy(ID_DESC);
        }
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    /**
     * 条件分页查询(不查总数)
     * 
     * @param page
     * @param rows
     * @param example
     * @return
     */
    public PageInfo<T> queryPageListByExampleWithoutCount(Integer page, Integer rows, Example example) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
    	return pageListByExample(page, rows, example, false);
    }
    
    /**
     * 条件分页查询(不查总数)
     * 
     * @param page
     * @param rows
     * @param example
     * @param desc 是否倒序 （true倒序， false升序）
     * @return
     */
    public PageInfo<T> pageListByExampleWithoutCount(Integer page, Integer rows, Example example, boolean desc) {
    	page = (page == null || page <= 0) ? PAGE : page;
    	rows = (rows == null || rows <= 0) ? ROWS : rows;
        PageHelper.startPage(page, rows, false);   //false表示不进行count查询
        if(desc) {
        	PageHelper.orderBy(ID_DESC);
        }
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
    	return this.mapper.existsWithPrimaryKey(id);
    }
    
    public boolean existsById(Long id){
    	return this.mapper.existsWithPrimaryKey(id);
    }
    
    //=================新增===================//
    
    @Deprecated
    public Integer save(T record) {
    	record.setId(null);
    	record.setCreateDate(new Date());
    	record.setUpdateDate(record.getCreateDate());
    	record.setVersion(0);
    	record.setDataFlag(1);
        return this.mapper.insert(record);
    }

    public Integer saveSelective(T record) {
    	record.setId(null);
    	record.setCreateDate(new Date());
    	record.setUpdateDate(record.getCreateDate());
    	record.setVersion(0);
    	record.setDataFlag(1);
        return this.mapper.insertSelective(record);
    }
    
    public Integer batchSaveList(List<T> recordList) {
    	Date date = new Date();
    	for (T record : recordList) {
    		record.setId(null);
        	record.setCreateDate(date);
        	record.setUpdateDate(date);
        	record.setVersion(0);
        	record.setDataFlag(1);
		}
        return this.mapper.insertList(recordList);
    }
    
    //=================更新===================//
    
    @Deprecated
    public Integer updateById(T record) {
    	record.setUpdateDate(new Date());
        return this.mapper.updateByPrimaryKey(record);
    }

    public Integer updateSelectiveById(T record) {
    	record.setUpdateDate(new Date());
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    @Deprecated
    public Integer updateByExample(T record, Example example) {
    	record.setUpdateDate(new Date());
        return this.mapper.updateByExample(record, example);
    }

    public Integer updateSelectiveByExample(T record, Example example) {
    	record.setUpdateDate(new Date());
        return this.mapper.updateByExampleSelective(record, example);
    }

    
    /**
     * 数据更新，存在乐观锁控制
     * 
     * @param record
     * @param example
     * @return
     */
    public Integer updateSelectiveByVersion(T record, Example example) {
    	Assert.isNull(record.getVersion(), "版本号不能为空");
    	//版本号作为条件
    	example.and().andEqualTo("version", record.getVersion());
    	//通用更新字段
    	record.setUpdateDate(new Date());
    	record.setVersion(record.getVersion() + 1);
        return this.mapper.updateByExampleSelective(record, example);
    }
    
    
    //=================删除===================//
    
    public Integer removeById(Integer id) {
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    public Integer removeById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    public Integer removeByRecord(T record) {
        return this.mapper.delete(record);
    }
    
    public Integer removeByExample(Example example) {
        return this.mapper.deleteByExample(example);
    }
    
    public Integer removeByIds(List<?> ids, Class<T> clazz) {
    	Example example = new Example(clazz);
    	example.createCriteria().andIn(ID, ids);
        return this.mapper.deleteByExample(example);
    }
    
}
