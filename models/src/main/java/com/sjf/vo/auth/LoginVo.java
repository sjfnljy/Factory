package com.sjf.vo.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 用户登录实体传输类
 * @Author: SJF
 * @Date: 2023/1/13
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class LoginVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "提交的验证码")
    private String captcha;

    @ApiModelProperty(value = "提交的验证码 key")
    private String codeKey;
}
