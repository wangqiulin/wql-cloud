package com.wql.generator.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.wql.generator.entity.ColumnInfo;
import com.wql.generator.utils.ConfigUtil;

public class ConnectionUtil {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    /**
     * 初始化数据库连接
     *
     * @return
     */
    public boolean initConnection() {
        try {
            Class.forName(DriverFactory.getDriver(ConfigUtil.getConfiguration().getDb().getUrl()));
            String url = ConfigUtil.getConfiguration().getDb().getUrl();
            String username = ConfigUtil.getConfiguration().getDb().getUsername();
            String password = ConfigUtil.getConfiguration().getDb().getPassword();
            connection = DriverManager.getConnection(url, username, password);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ColumnInfo> getMetaData(String tableName) throws SQLException {
    	return getMetaData(null, tableName);
    }
    
    /**
     * 获取表结构数据
     *
     * @param tableName 表名
     * @return 包含表结构数据的列表
     */
    public List<ColumnInfo> getMetaData(String dbName, String tableName) throws SQLException {
        ResultSet tempResultSet = connection.getMetaData().getPrimaryKeys(dbName, null, tableName);
        String primaryKey = null;
        if (tempResultSet.next()) {
            primaryKey = tempResultSet.getObject(4).toString();
        }
        List<ColumnInfo> columnInfos = new ArrayList<>();
        statement = connection.createStatement();
        String sql = "SELECT * FROM " + tableName + " WHERE 1 != 1";
        resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            ColumnInfo info;
            if (metaData.getColumnName(i).equals(primaryKey)) {
                info = new ColumnInfo(metaData.getColumnName(i), metaData.getColumnType(i), true);
            } else {
                info = new ColumnInfo(metaData.getColumnName(i), metaData.getColumnType(i), false);
            }
            columnInfos.add(info);
        }
        statement.close();
        resultSet.close();
        return columnInfos;
    }

    public void close() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
