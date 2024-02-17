package com.sjf.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 角色分配菜单传输类
 * @Author: SJF
 * @Date: 2024/1/13 11:46
 */
@Data
@ApiModel(description = "角色分配菜单")
public class RoleAssignMenuVo implements Serializable {

    @ApiModelProperty(value = "角色 id")
    private Long roleId;

    @ApiModelProperty(value = "菜单 id 列表")
    private List<Long> menuIdList;
}
