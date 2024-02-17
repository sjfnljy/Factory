package com.sjf.constant;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 操作人类别常量枚举类
 * @Author: SJF
 * @Date: 2024/1/14 16:17
 */

public enum OperatorType {
        OTHER,
        @ApiModelProperty(value = "后台用户")
        MANAGE,
        @ApiModelProperty(value = "手机用户")
        MOBILE
}
