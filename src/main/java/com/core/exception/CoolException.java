package com.core.exception;

/**
 * 框架异常
 * Created by vincent on 2019-09-04
 */
public class CoolException extends RuntimeException {

    public CoolException(Throwable e) {
        super(e);
    }

    public CoolException(String message) {
        super(message);
    }

}
