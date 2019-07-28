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

	
	/**
	 * insert into table (aa,bb,cc) values(xx,xx,xx),(oo,oo,oo)
	 * @param ms
	 * @return
	 */
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
	
	

    /**
     * 拼update sql, 使用case when方式，id为主键
     *
     * @param ms
     * @return
     */
    public String updateBatchByPrimaryKeySelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        //开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"set\" suffixOverrides=\",\">");

        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        for (EntityColumn column : columnList) {
            if (!column.isId() && column.isUpdatable()) {
                sql.append("  <trim prefix=\""+column.getColumn()+" =case\" suffix=\"end,\">");
                sql.append("    <foreach collection=\"list\" item=\"i\" index=\"index\">");
                sql.append("      <if test=\"i."+column.getEntityField().getName()+"!=null\">");
                sql.append("         when id=#{i.id} then #{i."+column.getEntityField().getName()+"}");
                sql.append("      </if>");
                sql.append("    </foreach>");
                sql.append("  </trim>");
            }
        }

        sql.append("</trim>");
        sql.append("WHERE");
        sql.append(" id IN ");
        sql.append("<trim prefix=\"(\" suffix=\")\">");
        sql.append("<foreach collection=\"list\" separator=\", \" item=\"i\" index=\"index\" >");
        sql.append("#{i.id}");
        sql.append("</foreach>");
        sql.append("</trim>");
        return sql.toString();
    }
	
}
