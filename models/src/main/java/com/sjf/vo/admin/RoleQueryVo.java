package com.sjf.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 角色条件查询传输类
 * @Author: SJF
 * @Date: 2024/1/8 20:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "角色条件查询传输类")
public class RoleQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色编号")
    private String roleCode;
}
