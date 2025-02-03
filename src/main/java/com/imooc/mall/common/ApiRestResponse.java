package com.imooc.mall.common;

import com.imooc.mall.exception.ImoocMallExceptionEnum;

/**
 * 接口通用返回对象，用于接口各种情况下的返回
 * 规定接口返回结构为：
 * status: 状态码
 * msd: 状态信息
 * data: 对象数据
 */
public class ApiRestResponse<T> {
    private Integer status;
    private String msg;
    private T data;

    // 文档约定好的特定常量
    private static final int OK_CODE = 10000;
    private static final String OK_MSG = "SUCCESS";

    public ApiRestResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiRestResponse(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ApiRestResponse() {
        this(OK_CODE, OK_MSG); // 默认为成功返回
    }

    /**
     * 成功返回 -- 状态码、状态信息
     *
     * @param <T>
     * @return 统一返回对象
     */
    public static <T> ApiRestResponse<T> success() {
        return new ApiRestResponse<>();
    }

    /**
     * 成功返回 -- 状态码、状态信息、对象
     *
     * @param result 对象
     * @param <T>
     * @return 统一返回对象
     */
    public static <T> ApiRestResponse<T> success(T result) {
        ApiRestResponse<T> response = new ApiRestResponse<>();
        response.setData(result);
        return response;
    }

    /**
     * @param code 异常码
     * @param msg  异常信息
     * @param <T>
     * @return 统一返回对象
     */
    public static <T> ApiRestResponse<T> error(Integer code, String msg) {
        return new ApiRestResponse<>(code, msg);
    }

    /**
     * 异常返回
     *
     * @param ex  异常类枚举对象
     * @param <T>
     * @return 统一返回对象
     */
    public static <T> ApiRestResponse<T> error(ImoocMallExceptionEnum ex) {
        return new ApiRestResponse<>(ex.getCode(), ex.getMsg());
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "ApiRestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
