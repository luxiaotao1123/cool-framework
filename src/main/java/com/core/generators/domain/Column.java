package com.core.generators.domain;

import com.core.common.Cools;
import com.core.generators.utils.GeneratorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vincent on 2019-06-18
 */
public class Column {

    private String name; // 表字段名
    private String type; // 类型
    private String comment; // 备注
    private String humpName; // 小驼峰
    private boolean primaryKey; // 主键
    private List<Map<String, Object>> enums; // 枚举值

    public Column(String name, String type, String comment, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.comment = "";
        if (!Cools.isEmpty(comment)){
            Pattern pattern1 = Pattern.compile("(.+?)(?=\\{)");
            Matcher matcher1 = pattern1.matcher(comment);
            if (matcher1.find()){
                this.comment = matcher1.group();
                Pattern pattern2 = Pattern.compile("(?<=\\{)(.+?)(?=})");
                Matcher matcher2 = pattern2.matcher(comment);
                if (matcher2.find()){
                    String group = matcher2.group();
                    if (!Cools.isEmpty(group)){
                        String[] values = group.split(",");
                        this.enums = new ArrayList<>();
                        for (String val : values){
                            Map<String, Object> map = new HashMap<>();
                            String[] split = val.split(":");
                            map.put(split[0], split[1]);
                            enums.add(map);
                        }
                    }
                }
            } else {
                this.comment = comment;
            }

        }
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

    public List<Map<String, Object>> getEnums() {
        return enums;
    }

    public void setEnums(List<Map<String, Object>> enums) {
        this.enums = enums;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", humpName='" + humpName + '\'' +
                ", primaryKey=" + primaryKey +
                ", enums=" + enums +
                '}';
    }
}
