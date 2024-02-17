package com.sjf.entity.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 角色与菜单中间类
 * @Author: SJF
 * @Date: 2024/1/8 21:17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_role_menu")
public class RoleAndMenu extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("role_id")
    @ApiModelProperty(value = "角色 id")
    private Long roleId;

    @TableField("menu_id")
    @ApiModelProperty(value = "菜单 id")
    private Long menuId;

    public RoleAndMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
