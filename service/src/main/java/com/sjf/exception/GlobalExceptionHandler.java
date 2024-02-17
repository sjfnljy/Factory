package com.sjf.exception;


import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 全局异常处理
 * @Author: SJF
 * @Date: 2024/1/7
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> globalException(Exception exception){
        return Result.build(null, HttpCode.FAILURE,"出现全局异常,异常信息为：" + exception.getMessage());
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public Result<Object> customException(CustomException exception){
        return Result.build(null, HttpCode.FAILURE,"出现自定义异常,异常信息为：" + exception.getMessage());
    }

}
