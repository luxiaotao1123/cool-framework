package com.core.tools;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vincent on 2019-06-03
 */
public class ReflectTools {

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
     * 删除Field数组的静态成员变量
     */
    public static Field[] removeStaticField(Field[] params){
        List<Field> fields = new ArrayList<>(Arrays.asList(params));
        if (fields.size() == 0){
            return null;
        }
        for (int i = fields.size() - 1; i >= 0; i--) {
            if(fields.get(i) == null){
                fields.remove(i);
            } else if (Modifier.isStatic(fields.get(i).getModifiers())) {
                fields.remove(i);
                i++;
            }
        }
        return fields.toArray(new Field[0]);
    }

    /**
     * 递归获取成员变量对象（包含其基类）
     */
    public static Field getField(Class<?> cls, String fieldName){
        Field field;
        try {
            field = cls.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superCls = cls.getSuperclass();
            if (superCls == null || superCls == Object.class){
                return null;
            }
            return getField(superCls, fieldName);

        }
        return field;
    }

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

    private static <T> T[] clone(T[] array) {
        return array == null ? null : array.clone();
    }

}
