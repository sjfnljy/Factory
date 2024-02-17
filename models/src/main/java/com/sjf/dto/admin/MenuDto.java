package com.sjf.dto.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: 动态菜单返回前端传输类
 * @Author: SJF
 * @Date: 2024/1/18 18:34
 */

@Data
public class MenuDto {

    @ApiModelProperty(value = "路由地址")
    private String name;

    @ApiModelProperty(value = "路由名称")
    private String title;

    @ApiModelProperty(value = "子路由")
    private List<MenuDto> children;
}
