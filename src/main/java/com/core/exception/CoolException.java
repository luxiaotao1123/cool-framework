package com.core.exception;

/**
 * Created by vincent on 2019-06-03
 */
public class CoolException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CoolException(Throwable e) {
        super(e);
    }

    public CoolException(String message) {
        super(message);
    }

}
