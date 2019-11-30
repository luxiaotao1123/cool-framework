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

    public R(Integer code, String msg){
        super.put(CODE, code);
        super.put(MSG, msg);
    }

    public static R ok(){
        return parse(BaseRes.OK);
    }

    public static R ok(String msg){
        R r = ok();
        r.put(MSG, msg);
        return r;
    }

    public static R ok(Object obj){
        return parse(BaseRes.OK).add(obj);
    }

    public static R error(){
        return parse(BaseRes.ERROR);
    }

    public static R error(String msg){
        R r = error();
        r.put(MSG, msg);
        return r;
    }

    public R add(Object obj){
        this.put(DATA, obj);
        return this;
    }

    public static R parse(String message){
        if(Cools.isEmpty(message)){
            return parse(BaseRes.ERROR);
        }
        String[] msg = message.split("-");
        if(msg.length==2){
            return new R(Integer.parseInt(msg[0]),msg[1]);
        }else{
            return parse(BaseRes.ERROR);
        }
    }

}
