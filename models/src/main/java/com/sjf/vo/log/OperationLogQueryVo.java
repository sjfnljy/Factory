package com.sjf.vo.log;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 操作日志查询传输类
 * @Author: SJF
 * @Date: 2024/1/27 19:56
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class OperationLogQueryVo implements Serializable {
    private String title;
    private String method;
    private String requestMethod;
    private String operatorType;
    private String operationName;
}
