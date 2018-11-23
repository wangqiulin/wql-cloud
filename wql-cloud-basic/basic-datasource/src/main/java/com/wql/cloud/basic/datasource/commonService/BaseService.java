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
    
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    public List<T> queryList() {
        return this.mapper.select(null);
    }

    /**
     * 根据条件查询一条数据，如果有多条数据会抛出异常
     */
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    /**
     * 根据条件查询一条数据，如果有多条数据会抛出异常
     */
    public T queryOne(Example example) {
        List<T> list = this.mapper.selectByExample(example);
        if(CollectionUtils.isEmpty(list)) {
        	return null;
        }
        return list.get(0);
    }

    public List<T> queryListByWhere(T record) {
        return this.mapper.select(record);
    }

    /**
     * 根据条件查询数据列表
     */
    public List<T> queryListByExample(Example example) {
        return this.mapper.selectByExample(example);
    }

    /**
     * 分页查询
     */
    public PageInfo<T> queryPageListByWhere(Integer page, Integer rows, T record) {
        PageHelper.startPage(page, rows);
        PageHelper.orderBy("id desc");
        List<T> list = this.mapper.select(record);
        return new PageInfo<T>(list);
    }
    
    
    /**
     * 根据Example， 分页查询
     */
    public PageInfo<T> queryPageListByExample(Integer page, Integer rows, Example example) {
        PageHelper.startPage(page, rows);
        PageHelper.orderBy("id desc");
        List<T> list = this.mapper.selectByExample(example);
        return new PageInfo<T>(list);
    }
    

    /**
     * 根据code， 查询唯一一条记录数据
     */
    public T queryOneByCode(Class<T> clazz, String property, String value) {
        Example example = new Example(clazz);
        example.createCriteria().andEqualTo(property, value);
        List<T> list = mapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        throw new RuntimeException(clazz + "，只要一条记录，结果查询出" + list.size() + "条记录");
    }

    /**
     * 根据字段(等号关系)，查询数量
     */
    public int queryCountByWhere(T record) {
        return this.mapper.selectCount(record);
    }

    /**
     * 根据字段(等号关系)，查询数量
     */
    public int queryCountByExample(Example example) {
        return this.mapper.selectCountByExample(example);
    }

    /**
     * 新增数据，返回成功的条数
     */
    public int save(T record) {
        return this.mapper.insert(record);
    }

    /**
     * 新增数据，使用不为null的字段，返回成功的条数
     */
    public int saveSelective(T record) {
        return this.mapper.insertSelective(record);
    }

    /**
     * 根据主键id，修改数据，返回成功的条数
     */
    public int update(T record) {
        return this.mapper.updateByPrimaryKey(record);
    }

    /**
     * 根据主键id，修改数据，使用不为null的字段，返回成功的条数
     */
    public int updateSelective(T record) {
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键id，修改数据，返回成功的条数
     */
    public int updateByExample(T record, Example example) {
        return this.mapper.updateByExample(record, example);
    }

    /**
     * 根据主键id，修改数据，使用不为null的字段，返回成功的条数
     */
    public int updateByExampleSelective(T record, Example example) {
        return this.mapper.updateByExampleSelective(record, example);
    }


    /**
     * 根据id，删除数据
     */
    public int deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除数据
     */
    public int deleteByIds(Class<T> clazz, Example example) {
        return this.mapper.deleteByExample(example);
    }

    /**
     * 根据条件，删除数据
     */
    public int deleteByWhere(T record) {
        return this.mapper.delete(record);
    }

}
