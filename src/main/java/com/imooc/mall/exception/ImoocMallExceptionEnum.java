package com.imooc.mall.exception;

/**
 * 系统内各种自定义异常的枚举，方便统一管理
 */
public enum ImoocMallExceptionEnum {
    NEED_USER_NAME(10001, "用户名不能为空");

    Integer code; // 异常码
    String msg; // 异常信息

    ImoocMallExceptionEnum(Integer code, String msd) {
        this.code = code;
        this.msg = msd;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
