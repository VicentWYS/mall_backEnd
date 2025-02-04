package com.imooc.mall.exception;

// 统一异常类
public class ImoocMallException extends Exception {
    private final Integer code;
    private final String message;

    public ImoocMallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ImoocMallException(ImoocMallExceptionEnum em) {
        this(em.getCode(), em.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
