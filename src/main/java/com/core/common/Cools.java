package com.core.common;

import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
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

    /**
     * 截取字符串(默认end=true)
     * @param str 被截字符串
     * @param end true:最后一个字符 / false:第一个字符
     */
    public static String deleteChar(String str, boolean end){
        if (isEmpty(str)){
            return "";
        }
        if (end){
            return str.substring(0, str.length()-1);
        } else {
            return str.substring(1);
        }
    }

    public static String deleteChar(String str){
        return deleteChar(str, true);
    }


    /**
     * map 转 对象
     */
    public static <T> T conver(Map<? extends String, ?> map, Class<T> cls){
        T instance = null;
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor();
            boolean constructorAccessible = constructor.isAccessible();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
            constructor.setAccessible(constructorAccessible);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Class<?> prototype = cls;
        do {
            for (Field field : prototype.getDeclaredFields()){
                if (Modifier.isFinal(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers())
                        || Modifier.isTransient(field.getModifiers())){
                    continue;
                }
                String fieldName = field.getName();
                Object val = null;
                if (map.containsKey(fieldName)){
                    val = map.get(fieldName);
                }
                if (val != null){
                    boolean fieldAccessible = field.isAccessible();
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    try {
                        Constructor<?> constructor = type.getDeclaredConstructor(String.class);
                        boolean constructorAccessible = constructor.isAccessible();
                        constructor.setAccessible(true);
                        field.set(instance, constructor.newInstance(String.valueOf(val)));
                        constructor.setAccessible(constructorAccessible);
                    } catch (IllegalAccessException
                            | InstantiationException
                            | InvocationTargetException
                            | NoSuchMethodException e) {
                        System.err.println("convert error ===> Class["+prototype+"],Field:["+fieldName+"],Type:["+type+"],Value:["+val+"]");
                    }
                    field.setAccessible(fieldAccessible);
                }
            }
            prototype = prototype.getSuperclass();
        } while (!Object.class.equals(prototype));
        return instance;
    }

    /**
     * 对象 转 map
     */
    public static Map<String, Object> conver(Object obj){
        Class<?> cls = obj.getClass();
        Field[] fields = getAllFields(cls);
        Map<String, Object> map = new HashMap<>();
        for (Field field : fields) {
            String key = field.getName();
            boolean flag = field.isAccessible();
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(flag);
            if (val != null){
                map.put(key, val);
            }
        }
        return map;
    }

    /**
     * 获取指定Class（及其SuperClass）的成员变量
     */
    public static Field[] getAllFields(Class<?> cls){
        return getAllFields(cls, null);
    }

    /**
     * 递归合并基类Field
     */
    private static Field[] getAllFields(Class<?> cls, Field[] params){
        Field[] fields = (params == null) ? cls.getDeclaredFields() : params;
        Class<?> superCls = cls.getSuperclass();
        if (superCls == null || superCls == Object.class){
            return fields;
        }
        Field[] superClsFields = superCls.getDeclaredFields();
        fields = addAll(fields, superClsFields);
        return getAllFields(superCls, fields);
    }


    /**
     * 数组叠加
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] addAll(T[] array1, T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        } else {
            Class<?> cls = array1.getClass().getComponentType();
            T[] joinedArray = (T[]) Array.newInstance(cls, array1.length + array2.length);
            System.arraycopy(array1, 0, joinedArray, 0, array1.length);

            try {
                System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
                return joinedArray;
            } catch (ArrayStoreException e) {
                Class<?> type2 = array2.getClass().getComponentType();
                if (!cls.isAssignableFrom(type2)) {
                    throw new RuntimeException("Cannot store " + type2.getName() + " in an array of " + cls.getName(), e);
                } else {
                    throw e;
                }
            }
        }
    }

    /**
     * 克隆
     */
    private static <T> T[] clone(T[] array) {
        return array == null ? null : array.clone();
    }

    /**
     * map操作
     */
    public static CoolMap add(String key,Object value){
        CoolMap map = new CoolMap();
        map.put(key, value);
        return map;
    }

    public static class CoolMap extends HashMap<String, Object>{

        public CoolMap add(String key,Object value){
            this.put(key, value);
            return this;
        }

        public CoolMap $(String key,Object value){
            this.put(key, value);
            return this;
        }

    }

    public static String md5(String string){
        try{
            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] bytes=md5.digest(string.getBytes(StandardCharsets.UTF_8));
            char[] chars = new char[bytes.length * 2];
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i];
                chars[i * 2] = hexDigits[(b & 0xF0) >> 4];
                chars[i * 2 + 1] = hexDigits[b & 0x0F];
            }
            return new String(chars);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("md5加密失败,str=".concat(string));
        }
    }

    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


}
