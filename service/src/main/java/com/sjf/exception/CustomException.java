package com.sjf.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 自定义异常类
 * @Author: SJF
 * @Date: 2023/1/7
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomException extends RuntimeException{

    private final Integer code;

    private final String message;

    /**
     * 通过状态码和错误消息创建异常对象.
     */
    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
