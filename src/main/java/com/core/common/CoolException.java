package com.core.common;

/**
 * Created by vincent on 2019-06-09
 */
public class CoolException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;

    public CoolException(BaseRes baseRes) {
        super(baseRes.des);
        this.code = baseRes.code;
        this.msg = baseRes.des;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }
}
