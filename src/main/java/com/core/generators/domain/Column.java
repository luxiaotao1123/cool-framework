package com.core.generators.domain;

import com.core.common.Cools;
import com.core.generators.CoolGenerator;
import com.core.generators.utils.GeneratorUtils;

import java.sql.Connection;
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
    private boolean notNull; // 非空
    private boolean major; // 主要
    private boolean image; // 图片
    private String foreignKey; // 外健实例名(大驼峰,如sys_user ==> User)
    private String foreignKeyMajor; // 外键
    private List<Map<String, Object>> enums; // 枚举值
    private Integer length; // 字段长度

    public Column(Connection conn, String name, String type, String comment, boolean primaryKey, boolean notNull, Integer length) {
        this.name = name;
        this.type = type;
        this.comment = "";
        if (!Cools.isEmpty(comment)){
            // 枚举
            Pattern pattern1 = Pattern.compile("(.+?)(?=\\{)");
            Matcher matcher1 = pattern1.matcher(comment);
            // 外键
            Pattern pattern11 = Pattern.compile("(.+?)(?=\\[)");
            Matcher matcher11 = pattern11.matcher(comment);
            // 枚举
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
            //  外键
            } else if (matcher11.find()){
                this.comment = matcher11.group();
                Pattern pattern22 = Pattern.compile("(?<=\\[)(.+?)(?=])");
                Matcher matcher22 = pattern22.matcher(comment);
                if (matcher22.find()) {
                    String group = matcher22.group();
                    if (!Cools.isEmpty(group)) {
                        this.foreignKey = GeneratorUtils.getNameSpace(group);
                        List<Column> foreignColumns = new ArrayList<>();
                        try {
                            foreignColumns = CoolGenerator.getColumns(conn, group);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        if (!Cools.isEmpty(foreignColumns)){
                            for (Column column : foreignColumns){
                                if (column.isMajor()){
                                    this.foreignKeyMajor = GeneratorUtils.firstCharConvert(column.getHumpName(), false);
                                }
                            }
                        }
                    }
                }
            } else {
                this.comment = comment;
            }
            // 主要字段
            if (comment.endsWith("(*)")){
                this.comment = comment.substring(0, comment.length()-3);
                this.major = true;
            }
            // 图片字段
            if (comment.endsWith("(img)")){
                this.comment = comment.substring(0, comment.length()-5);
                this.image = true;
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

    public String getForeignKeyMajor() {
        return foreignKeyMajor;
    }

    public void setForeignKeyMajor(final String foreignKeyMajor) {
        this.foreignKeyMajor = foreignKeyMajor;
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

    public boolean isMajor() {
        return major;
    }

    public void setMajor(final boolean major) {
        this.major = major;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(final boolean image) {
        this.image = image;
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
                ", major=" + major +
                ", image=" + image +
                ", foreignKey='" + foreignKey + '\'' +
                ", foreignKeyMajor='" + foreignKeyMajor + '\'' +
                ", enums=" + enums +
                ", length=" + length +
                '}';
    }

}
