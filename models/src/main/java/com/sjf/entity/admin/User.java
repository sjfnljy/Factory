package com.sjf.entity.admin;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;



/**
 * @Description: 用户实体类
 * @Author: SJF
 * @Date: 2024/1/9 22:24
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_admin_user")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "手机号码")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "头像地址")
    @TableField("head_url")
    private String headUrl;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "状态（1：正常 0：停用）")
    @TableField("status")
    private Integer status;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private Long createRoleId;

    @TableField(exist = false)
    @JSONField(deserialize = false)
    private Long queryRoleId;
}
