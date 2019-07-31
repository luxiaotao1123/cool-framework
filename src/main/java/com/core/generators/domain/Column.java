package com.core.generators.domain;

import com.core.generators.utils.GeneratorUtils;

/**
 * Created by vincent on 2019-06-18
 */
public class Column {

    private String name; // 表字段名
    private String type; // 类型
    private String comment; // 备注
    private String humpName; // 小驼峰
    private boolean primaryKey; // 主键

    public Column(String name, String type, String comment, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.comment = comment;
        this.primaryKey = primaryKey;
        this.humpName = GeneratorUtils._convert(name);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getHumpName() {
        return humpName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", humpName='" + humpName + '\'' +
                ", primaryKey=" + primaryKey +
                '}';
    }
}
