package com.sjf.entity.admin;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 系统角色实体类
 * @Author: SJF
 * @Date: 2023/1/7
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_admin_role")
@ApiModel(value = "系统角色实体类")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("role_name")
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @TableField("role_code")
    @ApiModelProperty(value = "角色编码，唯一")
    private String roleCode;


    @TableField("description")
    @ApiModelProperty(value = "角色描述")
    private String description;

    @TableField(exist = false)
    @JSONField(deserialize = false)
    private List<Long> queryMenuIdList;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private List<Long> createMenuIdList;

}
