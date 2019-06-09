package com.core.common;

import java.util.HashMap;

/**
 * Created by vincent on 2019-06-09
 */
public class R extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    private static final String CODE = "code";
    private static final String MSG = "msg";
    private static final String DATA = "data";

    public R(){
        this(CodeRes.OK);
    }

    public R(String msg){
        super.put(CODE, CodeRes.ERROR.code);
        super.put(MSG, msg);
    }

    private R(CodeRes codeRes) {
        super.put(CODE, codeRes.code);
        super.put(MSG, codeRes.des);
    }

    public static R ok(){
        return new R();
    }

    public static R ok(Object obj){
        R r = new R();
        r.put(DATA, obj);
        return r;
    }

    public static R error(){
        return new R(CodeRes.ERROR);
    }

    public static R error(String msg){
        return new R(msg);
    }
}
