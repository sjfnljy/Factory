package com.sjf.entity.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 菜单实体类
 * @Author: SJF
 * @Date: 2024/1/12 18:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_admin_menu")
@ApiModel(value = "菜单实体类")
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("parent_id")
    @ApiModelProperty(value = "父组件 id")
    private Long parentId;

    @TableField("name")
    @ApiModelProperty(value = "名称")
    private String name;

    @TableField("type")
    @ApiModelProperty(value = "类型(0: 顶层路由, 1:菜单, 2:按钮 )")
    private Integer type;

    @TableField("path")
    @ApiModelProperty(value = "路由地址")
    private String component;

    @TableField("perms")
    @ApiModelProperty(value = "权限标识")
    private String perms;

    @TableField("sort_value")
    @ApiModelProperty(value = "排序")
    private Integer sortValue;

    @TableField("status")
    @ApiModelProperty(value = "状态(0:正常,1:禁止)")
    private Integer status;

    @ApiModelProperty(value = "组件下级菜单列表")
    @TableField(exist = false)
    private List<Menu> children;
}
