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

    public static void main(String[] args) {
        String s  ="会员编号";
        String substring = s.substring(0, s.length()-2);
        System.out.println(substring);
//        new Column(null, null , "会员编号[sys_user]", false, false);
    }

    private String name; // 表字段名
    private String type; // 类型
    private String comment; // 备注
    private String humpName; // 小驼峰
    private boolean primaryKey; // 主键
    private boolean notNull; // 非空
    private String foreignKey; // 外健实例名(大驼峰,如sys_user ==> User)
    private List<Map<String, Object>> enums; // 枚举值
    private Integer length; // 字段长度

    public Column(String name, String type, String comment, boolean primaryKey, boolean notNull, Integer length) {
        this.name = name;
        this.type = type;
        this.comment = "";
        if (!Cools.isEmpty(comment)){
            Pattern pattern1 = Pattern.compile("(.+?)(?=\\{)");
            Pattern pattern11 = Pattern.compile("(.+?)(?=\\[)");
            Matcher matcher1 = pattern1.matcher(comment);
            Matcher matcher11 = pattern11.matcher(comment);
            if (matcher1.find()) {
                this.comment = matcher1.group();
                Pattern pattern2 = Pattern.compile("(?<=\\{)(.+?)(?=})");
                Matcher matcher2 = pattern2.matcher(comment);
                if (matcher2.find()) {
                    String group = matcher2.group();
                    if (!Cools.isEmpty(group)) {
                        String[] values = group.split(",");
                        this.enums = new ArrayList<>();
                        for (String val : values) {
                            Map<String, Object> map = new HashMap<>();
                            String[] split = val.split(":");
                            map.put(split[0], split[1]);
                            enums.add(map);
                        }
                    }
                }
            } else if (matcher11.find()){
                this.comment = matcher11.group();
                Pattern pattern22 = Pattern.compile("(?<=\\[)(.+?)(?=])");
                Matcher matcher22 = pattern22.matcher(comment);
                if (matcher22.find()) {
                    String group = matcher22.group();
                    if (!Cools.isEmpty(group)) {
                        this.foreignKey = GeneratorUtils.getNameSpace(group);
                    }
                }
            } else {
                this.comment = comment;
            }
        }
        this.primaryKey = primaryKey;
        this.notNull = notNull;
        this.length = length;
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

    public String getWholeComment() {
        if (!Cools.isEmpty(this.enums)){
            StringBuilder sb = new StringBuilder(" ");
            for (Map<String, Object> val : enums){
                for (Map.Entry<String, Object> entry : val.entrySet()){
                    sb.append(entry.getKey())
                            .append(": ")
                            .append(entry.getValue())
                            .append("  ");
                }
            }
            return comment + sb.toString();
        }
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

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(final boolean notNull) {
        this.notNull = notNull;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(final String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public List<Map<String, Object>> getEnums() {
        return enums;
    }

    public void setEnums(List<Map<String, Object>> enums) {
        this.enums = enums;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(final Integer length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", humpName='" + humpName + '\'' +
                ", primaryKey=" + primaryKey +
                ", notNull=" + notNull +
                ", foreignKey='" + foreignKey + '\'' +
                ", enums=" + enums +
                '}';
    }
}
