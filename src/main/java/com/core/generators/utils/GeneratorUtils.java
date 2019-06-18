package com.core.generators.utils;

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

}
