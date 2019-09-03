package com.core.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 2019-06-09
 */
public class BaseController {

    public <T> List exportSupport(List<T> list, List<String> fields){
        try {
            List<List<Object>> result = new ArrayList<>();
            Method[] methods = list.get(0).getClass().getMethods();
            for (T t : list){
                List<Object> node = new ArrayList<>();
                for (String field : fields){
                    for (Method method : methods) {
                        if (("get" + field).toLowerCase().equals(method.getName().toLowerCase())) {
                            Object val = method.invoke(t);
                            node.add(val);
                            break;
                        }
                    }
                }
                result.add(node);
            }
            return result;
        } catch (Exception e){
            throw new RuntimeException();
        }

    }

}
