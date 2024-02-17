package com.sjf.vo.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 用户条件查询传输类
 * @Author: SJF
 * @Date: 2024/1/9 22:51
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class UserQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统用户名")
    private String username;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户电话号码")
    private String phone;

    @ApiModelProperty(value = "创建起始时间")
    private String createTimeBegin;

    @ApiModelProperty(value = "创建结束时间")
    private String createTimeEnd;
}
