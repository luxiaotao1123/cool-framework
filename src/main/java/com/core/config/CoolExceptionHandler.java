//package com.core.config;
//
//import com.core.common.R;
//
///**
// * Created by vincent on 2019-06-09
// */
//public class CoolExceptionHandler {
//
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public R handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
//        R r = new R();
//        r.put(STATUS, HttpStatusEnum.GATEWAY_REQUEST_METHOD_ERROR.getCode());
//        r.put(MSG, HttpStatusEnum.GATEWAY_REQUEST_METHOD_ERROR.getValue());
//        return r;
//    }
//
//    @ExceptionHandler(GatewayException.class)
//    public R handleRRException(GatewayException e) {
//        R r = new R();
//        r.put(STATUS, e.getStatus());
//        r.put(MSG, e.getMessage());
//        return r;
//    }
//
//}
