package com.sjf.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 为用户分配角色传输类
 * @Author: SJF
 * @Date: 2024/1/9 13:38
 */

@Data
@ApiModel(description = "用户分配角色")
public class UserAssignRoleVo implements Serializable {

    @ApiModelProperty(value = "用户 id")
    private Long userId;

    @ApiModelProperty(value = "角色 id ")
    private Long roleId;
}
