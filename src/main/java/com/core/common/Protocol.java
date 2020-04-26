package com.core.common;


import java.io.Serializable;

/**
 * common result
 * Created by vincent on 2020-04-09
 */
public class Protocol<T> implements Serializable {

    private static final long serialVersionUID = 4893280118017319089L;

    private int code;

    private String msg;

    private T data;

    public Protocol() {
    }

    public Protocol(int code, String msg, T data) {
        super();
        setCode(code);
        setMsg(msg);
        setData(data);
    }

    public Protocol(int code, String message) {
        this(code, message, null);
    }

    public static <T> Protocol<T> ok(){
        return parse(BaseRes.OK);
    }

    public static <T> Protocol<T> ok(T result){
        Protocol<T> protocol = parse(BaseRes.OK);
        protocol.setData(result);
        return protocol;
    }

    public static <T> Protocol<T> error(){
        return parse(BaseRes.ERROR);
    }

    public static <T> Protocol<T> error(String message) {
        Protocol<T> protocol = parse(BaseRes.ERROR);
        protocol.setMsg(message);
        return protocol;
    }

    public static <T> Protocol<T> parse(String str) {
        if(Cools.isEmpty(str)){
            return parse(BaseRes.ERROR);
        }
        String[] msg = str.split("-");
        if(msg.length==2){
            return new Protocol<>(Integer.parseInt(msg[0]),msg[1]);
        }else{
            return parse("500-".concat(str));
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
