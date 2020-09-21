package com.wql.generator.utils;

import java.sql.Types;

public class TypeUtil {

    /**
     * 将数据库数据类型转换为Java数据类型
     *
     * @param sqlType
     * @return
     */
    public static String parseTypeFormSqlType(int sqlType) {
        StringBuilder sb = new StringBuilder();
        switch (sqlType) {
            case Types.BIT:
            case Types.BOOLEAN:
                //sb.append("Boolean");
                sb.append("Integer");
                break;
            case Types.TINYINT:
                //sb.append("byte");
            	sb.append("Integer");
                break;
            case Types.SMALLINT:
                //sb.append("short");
            	sb.append("Integer");
                break;
            case Types.INTEGER:
                sb.append("Integer");
                break;
            case Types.BIGINT:
                sb.append("Long");
                break;
            case Types.REAL:
                sb.append("Float");
                break;
            case Types.FLOAT:
            case Types.DOUBLE:
                sb.append("Double");
                break;
            case Types.DECIMAL:
            case Types.NUMERIC:
                sb.append("BigDecimal");
                break;
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
                sb.append("String");
                break;
            case Types.DATE:
                sb.append("Date");
                break;
            case Types.TIME:
                sb.append("Time");
                break;
            case Types.TIMESTAMP:
                //sb.append("Timestamp");
                sb.append("Date");
                break;
            case Types.NCLOB:
            case Types.CLOB:
            case Types.BLOB:
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                sb.append("byte[]");
                break;
            case Types.NULL:
            case Types.OTHER:
            case Types.JAVA_OBJECT:
                sb.append("Object");
                break;
            default:
                sb.append("Object");
        }
        return sb.toString();
    }

}
