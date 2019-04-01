package com.wql.cloud.basic.datasource.tk.special;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class SpecialBatchProvider extends MapperTemplate {

	public SpecialBatchProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String batchInsertList(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
		sql.append(" VALUES ");
		sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
		sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (EntityColumn column : columnList) {
			if (column.isInsertable()) {
				sql.append(column.getColumnHolder("record") + ",");
			}
		}
		sql.append("</trim>");
		sql.append("</foreach>");
		return sql.toString();
	}

}
