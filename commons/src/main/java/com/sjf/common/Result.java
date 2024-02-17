package com.sjf.common;

import com.sjf.constant.HttpCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @Description: 返回结果封装类
 * @Author: SJF
 * @Date: 2024/1/7 21:41
 */
@Data
@ApiModel(description = "响应结果封装类")
public class Result <T>{

    @ApiModelProperty(value = "响应状态码")
    private Integer code;

    @ApiModelProperty(value = "响应结果信息")
    private String message;

    @ApiModelProperty(value = "响应返回数据")
    private T data;

    public Result(){}

    @ApiOperation(value = "构建没有返回数据返回的 Result 对象")
    public static <T> Result<T> build( Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    @ApiOperation(value = "构建带有返回数据返回的 Result 对象")
    public static <T> Result<T> build(T data, Integer code, String message) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    @ApiOperation(value = "构建响应成功且有返回数据返回的 Result 对象")
    public static<T> Result<T> ok(T data){
        return build(data, HttpCode.SUCCESS,"操作成功");
    }

    @ApiOperation(value = "构建响应成功且没有返回数据返回的 Result 对象")
    public static<T> Result<T> ok(){
        return Result.ok(null);
    }

    @ApiOperation(value = "构建响应失败但有返回数据返回的 Result 对象")
    public static<T> Result<T> fail(T data){
        return build(data, HttpCode.FAILURE,"操作失败");
    }

    @ApiOperation(value = "构建响应失败且有返回数据返回的 Result 对象")
    public static<T> Result<T> fail(){
        return Result.fail(null);
    }
}
