package com.core.generators.utils;

import com.core.common.Cools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.sql.Types.*;

/**
 * Created by vincent on 2019-06-18
 */
public class GeneratorUtils {

    /**
     * 下划线 ===>> 驼峰命名
     * @param smallHump 小驼峰命名
     */
    public static String _convert(String str, boolean smallHump){
        String[] split = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<split.length;i++){
            sb.append(i==0&&smallHump?split[i]:split[i].substring(0, 1).toUpperCase()+split[i].substring(1));
        }
        return sb.toString();
    }

    public static String _convert(String str){
        return _convert(str, true);
    }

    // sql类型 ===>> java类型
    public static String getType(int type){
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

    // sql表名 ===>> 去前缀 大驼峰
    public static String getNameSpace(String tableName){
        String[] strings = tableName.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i=1;i<strings.length;i++){
            if (i!=1){
                sb.append("_");
            }
            sb.append(strings[i]);
        }
        return _convert(sb.toString(), false);
    }

    // htmlDetail 字符适配
    public static String supportHtmlName(String comment){
        if (Cools.isEmpty(comment)){
            return "";
        }
        if (comment.length() == 2){
            return comment.charAt(0) + "　　" + comment.charAt(1);
        } else if (comment.length() == 3){
            return comment.charAt(0) + " " + comment.charAt(1) + " " +comment.charAt(2);
        }
        return comment;
    }

    /**
     * 获取mysql表字段长度
     */
    public static Integer getColumnLength(String typeMsg){
        if (Cools.isEmpty(typeMsg)){
            return null;
        }
        Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
        Matcher matcher = pattern.matcher(typeMsg);
        if (matcher.find()){
            return Integer.parseInt(matcher.group());
        }
        return null;
    }

    /**
     * 字符串首字母大小写转换
     * @param str 字符串
     * @param low true：小写   /   false：大写
     * @return the result
     */
    public static String firstCharConvert(String str, boolean low){
        if (Cools.isEmpty(str)){
            return "";
        }
        String firstChar = str.substring(0, 1);
        if (low){
            firstChar = firstChar.toLowerCase();
        } else {
            firstChar = firstChar.toUpperCase();
        }
        return firstChar + str.substring(1);
    }

    public static String firstCharConvert(String str){
        return firstCharConvert(str, true);
    }

}
