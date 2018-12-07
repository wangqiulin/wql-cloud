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
    
    public T queryById(Integer id) {
        return this.mapper.selectByPrimaryKey(id);
    }
    
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }
    
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    public T queryOne(Example example) {
        List<T> list = this.mapper.selectByExample(example);
        if(list != null && list.size() > 1) {
        	throw new RuntimeException("【ERROR】find more than one record");
        }
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
    
    public List<T> queryList() {
        return this.mapper.selectAll();
    }

    public List<T> queryListByRecord(T record) {
        return this.mapper.select(record);
    }

    public List<T> queryListByExample(Example example) {
        return this.mapper.selectByExample(example);
    }

    //=================分页查询===================//
    
    /**
     * 升序查询
     * @param page
     * @param rows
     * @param record
     * @return
     */
    public PageInfo<T> queryPageListByRecord(Integer page, Integer rows, T record) {
    	if(page == null || page <= 0) {
    		page = 1;
    	}
    	if(rows == null) {
    		rows = 10;
    	}
    	return queryPageListByRecord(page, rows, record, false);
    }
    
    /**
     * 根据desc值，是否倒序查询
     * @param page
     * @param rows
     * @param record
     * @param desc  是否倒序 （true倒序， false升序）
     * @return
     */
    public PageInfo<T> queryPageListByRecord(Integer page, Integer rows, T record, boolean desc) {
    	if(page == null || page <= 0) {
    		page = 1;
    	}
    	if(rows == null) {
    		rows = 10;
    	}
    	PageHelper.startPage(page, rows);
        if(desc) {
        	PageHelper.orderBy("id desc");
        }
        return new PageInfo<T>(this.mapper.select(record));
    }
    
    /**
     * 升序查询
     * @param page
     * @param rows
     * @param example
     * @return
     */
    public PageInfo<T> queryPageListByExample(Integer page, Integer rows, Example example) {
    	if(page == null || page <= 0) {
    		page = 1;
    	}
    	if(rows == null) {
    		rows = 10;
    	}
    	return queryPageListByExample(page, rows, example, false);
    }
    
    /**
     * 根据desc值，是否倒序查询
     * @param page
     * @param rows
     * @param example
     * @param desc 是否倒序 （true倒序， false升序）
     * @return
     */
    public PageInfo<T> queryPageListByExample(Integer page, Integer rows, Example example, boolean desc) {
    	if(page == null || page <= 0) {
    		page = 1;
    	}
    	if(rows == null) {
    		rows = 10;
    	}
        PageHelper.startPage(page, rows);
        if(desc) {
        	PageHelper.orderBy("id desc");
        }
        return new PageInfo<T>(this.mapper.selectByExample(example));
    }

    //=================查询数量===================//	
    
    public int queryCountByRecord(T record) {
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
    
    public int deleteById(Integer id) {
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    public int deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    public int deleteByIds(List<Integer> ids, Class<T> clazz) {
    	Example example = new Example(clazz);
    	example.createCriteria().andIn("id", ids);
        return this.mapper.deleteByExample(example);
    }
    
    public int deleteByIds2(List<Long> ids, Class<T> clazz) {
    	Example example = new Example(clazz);
    	example.createCriteria().andIn("id", ids);
        return this.mapper.deleteByExample(example);
    }
    
    public int deleteByRecord(T record) {
        return this.mapper.delete(record);
    }
    
    public int deleteByExample(Example example) {
        return this.mapper.deleteByExample(example);
    }
    
}
