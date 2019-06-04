package com.core.sql.table;

/**
 * 表字段
 * Created by vincent on 2019-06-03
 */
public class TableProperty {

    // 字段名
    private String fieldName;

    // 字段类型
    private Class<?> fieldType;

    public TableProperty() {
    }

    public TableProperty(String fieldName, Class<?> fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(final Class<?> fieldType) {
        this.fieldType = fieldType;
    }
}
