package com.core.generators;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static java.sql.Types.*;

/**
 * Created by vincent on 2019-06-09
 */
public class DataSourceRe {

    public static void main(String[] args) throws Exception {
        Connection conn;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/service", "root", "xltys1995");

        // 表
        List<String> tables = new ArrayList<String>();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(null, null, null,new String[] { "TABLE" });
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        TreeMap<String, String> treeMap = new TreeMap<>();

        // 表字段
        for (String tableName : tables) {
            String sql = "select * from " + tableName;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSetMetaData meta = ps.executeQuery().getMetaData();
            // 单表字段数量
            int columeCount = meta.getColumnCount();
            for (int i = 1; i < columeCount + 1; i++) {
                treeMap.put(meta.getColumnName(i), getType(meta.getColumnType(i)));
            }
        }
        System.out.println(treeMap.toString());
    }

    private static String getType(int type){
        switch (type){
            case BIT:
                return "Boolean";
            case TINYINT:
                return "Short";
            case INTEGER:
                return "Integer";
            case BIGINT:
                return "Long";
            case DOUBLE:
                return "Double";
            case DECIMAL:
                return "Double";
            case CHAR:
                return "String";
            case VARCHAR:
                return "String";
            case DATE:
                return "Date";
            case TIMESTAMP:
                return "Date";
            case BLOB:
                return "String";
            default:
                return null;
        }
    }
}
