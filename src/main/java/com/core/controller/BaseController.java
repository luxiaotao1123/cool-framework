package com.core.controller;

import com.core.common.BaseRes;
import com.core.common.Cools;
import com.core.exception.CoolException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vincent on 2019-06-09
 */
public class BaseController {

    public <T> List exportSupport(List<T> list, List<String> fields){
        if (Cools.isEmpty(list)){
            throw new CoolException(BaseRes.EMPTY);
        }
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

    public static Map<String, Object> excludeTrash(Map<String, Object> map){
        if (Cools.isEmpty(map)){
            return new HashMap<>();
        }
        map.entrySet().removeIf(next -> next.getKey().equals("curr")
                || next.getKey().equals("limit")
                || Cools.isEmpty(next.getValue()));
        return map;
    }

}
