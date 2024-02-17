package com.sjf.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 登录成功之后后端返回结果类
 * @Author: SJF
 * @Date: 2024/1/16 15:04
 */
@Data
@ApiModel("登录成功之后后端返回结果类")
public class LoginDto {

    @ApiModelProperty(value = "返回的 token")
    private String token;

    @ApiModelProperty(value = "刷新 token")
    private String refresh_token;
}
