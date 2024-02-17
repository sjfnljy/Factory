package com.sjf.entity.admin;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 用户和角色中间表
 * @Author: SJF
 * @Date: 2024/1/9 12:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user_role")
public class UserAndRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableField("user_id")
    @ApiModelProperty(value = "用户 id")
    private Long userId;

    @TableField("role_id")
    @ApiModelProperty(value = "角色 id")
    private Long roleId;

    public UserAndRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
