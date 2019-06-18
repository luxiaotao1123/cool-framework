package com.core.generators.domain;

import com.core.generators.utils.GeneratorUtils;

/**
 * Created by vincent on 2019-06-18
 */
public class Column {

    private String name;
    private String type;
    private String comment;
    private String humpName;

    public Column(String name, String type, String comment) {
        this.name = name;
        this.type = type;
        this.comment = comment;
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

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", humpName='" + humpName + '\'' +
                '}';
    }
}
