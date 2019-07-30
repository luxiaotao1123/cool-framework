package com.core.common;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vincent on 2019-06-09
 */
public class Cools {

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            if (o.toString().trim().equals("")) {
                return true;
            }
        } else if (o instanceof List) {
            if (((List) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Set) {
            if (((Set) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Object[]) {
            if (((Object[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof int[]) {
            if (((int[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof long[]) {
            if (((long[]) o).length == 0) {
                return true;
            }
        }
        return false;
    }

    public static int sqlLimitIndex(Integer pageIndex, Integer pageSize){
        return (pageIndex - 1) * pageSize;
    }

    public static String enToken(String username, String password){
        return AesUtils.encrypt(username, zerofill(password, 16));
    }

    public static String deTokn(String token, String password){
        return AesUtils.decrypt(token, zerofill(password, 16));
    }

    public static String zerofill(String msg, Integer count){
        if (msg.length() == count){
            return msg;
        } else if (msg.length() > count){
            return msg.substring(0, 16);
        } else {
            StringBuilder msgBuilder = new StringBuilder(msg);
            for (int i = 0; i<count-msg.length(); i++){
                msgBuilder.append("0");
            }
            return msgBuilder.toString();
        }
    }

}
