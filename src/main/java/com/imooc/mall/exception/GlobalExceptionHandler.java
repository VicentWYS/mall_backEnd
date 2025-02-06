package com.imooc.mall.exception;

import com.imooc.mall.common.ApiRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 当Controller中出现错误时，可直接返回ApiRestResponse对象的error()；
 * 但是，当Service层的Impl方法中要返回错误情况时，它将抛出自定义的异常类ImoocMallException(继承自Exception)；
 * 因此，此处需要将这个异常类也包装为ApiRestResponse对象的error()
 */
@ControllerAdvice // 拦截异常
public class GlobalExceptionHandler {
    private final Logger log = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 系统异常
     *
     * @param e 系统异常类对象 Exception
     * @return ApiRestResponse对象
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody // 返回json格式
    public Object handleException(Exception e) {
        // 打印为日志
        log.error("Default Exception: ", e); // 此处不需研究原理，直接使用即可

        // 将异常包装为ApiRestResponse对象
        return ApiRestResponse.error(ImoocMallExceptionEnum.SYSTEM_ERROR);
    }

    /**
     * 业务异常
     *
     * @param e 业务异常类对象 ImoocMallException
     * @return ApiRestResponse对象
     */
    @ExceptionHandler(ImoocMallException.class)
    @ResponseBody
    public Object handleImoocMallException(ImoocMallException e) {
        // 打印为日志
        log.error("ImoocMall Exception: ", e); // 此处不需研究原理，直接使用即可

        // 将异常包装为ApiRestResponse对象
        return ApiRestResponse.error(e.getCode(), e.getMessage()); // 可返回不同业务异常情况
    }

    /**
     * 系统异常：参数校验错误
     *
     * @param e 对象属性校验错误异常类
     * @return ApiRestResponse 对象
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 打印为日志
        log.error("MethodArgumentNotValidException: ", e); // 此处不需研究原理，直接使用即可

        // 将异常包装为ApiRestResponse对象
        return handelBindingResult(e.getBindingResult()); // 包含了所有参数校验失败的详细信息
    }

    // 转换：异常信息 -> ApiRestResponse对象
    private ApiRestResponse handelBindingResult(BindingResult result) {
        List<String> list = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String message = objectError.getDefaultMessage();
                list.add(message);
            }
        }

        if (list.size() == 0) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR);
        }

        return ApiRestResponse.error(ImoocMallExceptionEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }

}
