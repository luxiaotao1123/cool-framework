package com.core.common;

public enum BaseRes {

    OK(200, "操作成功"),
    DENIED(403, "无权限"),
    ERROR(500, "服务器错误"),

    ;

    public int code;
    public String des;

    BaseRes(final int code, final String des) {
        this.code = code;
        this.des = des;
    }

}
