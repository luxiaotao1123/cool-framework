package com.core.sql.table;

import java.util.List;

/**
 * 表实例
 * Created by vincent on 2019-06-03
 */
public class TableObject {

    // 表名
    private String tableName;

    // 表字段
    private List<TableProperty> properties;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    public List<TableProperty> getProperties() {
        return properties;
    }

    public void setProperties(final List<TableProperty> properties) {
        this.properties = properties;
    }
}
