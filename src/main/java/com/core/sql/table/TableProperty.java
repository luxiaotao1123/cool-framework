package com.core.sql.table;

import java.lang.reflect.Field;

/**
 * 表字段
 * Created by vincent on 2019-06-03
 */
public class TableProperty {

    // 字段名
    private String fieldName;

    // 字段类型
    private Field field;

    public TableProperty() {
    }

    public TableProperty(final String fieldName, final Field field) {
        this.fieldName = fieldName;
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    public Field getField() {
        return field;
    }

    public void setField(final Field field) {
        this.field = field;
    }
}
