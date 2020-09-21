package com.wql.generator.entity;

import java.io.Serializable;

import com.wql.generator.utils.StringUtil;

public class ColumnInfo implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private String columnName; // 列名
    private int type; // 类型代码
    private String propertyName; // 属性名
    private boolean isPrimaryKey; // 是否主键
    private String comment; //字段备注
    
    public ColumnInfo() {

    }

    public ColumnInfo(String columnName, int type, boolean isPrimaryKey, String comment) {
        this.columnName = columnName;
        this.type = type;
        this.propertyName = StringUtil.columnName2PropertyName(columnName);
        this.isPrimaryKey = isPrimaryKey;
        this.comment = comment;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
