package com.sjf.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 生成验证码传输类
 * @Author: SJF
 * @Date: 2024/1/15 20:41
 */

@Data
@ApiModel("后端生成验证码传输类")
public class ValidateCodeDto {

    @ApiModelProperty(value = "验证码 key 值")
    private String codeKey;

    @ApiModelProperty(value = "验证码 value 值")
    private String codeValue;
}
