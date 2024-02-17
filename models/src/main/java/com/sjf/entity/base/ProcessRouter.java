package com.sjf.entity.base;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 工艺路线实体类
 * @Author: SJF
 * @Date: 2024/1/19
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_process_router")
public class ProcessRouter extends BaseEntity {

    @TableField("router_code")
    @ApiModelProperty("工艺路线编号")
    @ExcelProperty(value = "工艺路线编号", index = 0)
    private String routerCode;

    @TableField("router_name")
    @ApiModelProperty("工艺路线名称")
    @ExcelProperty(value = "工艺路线名称", index = 1)
    private String routerName;

    @TableField(exist = false)
    @ApiModelProperty("该工艺路线对应工序 id 集合")
    private List<Long> processIdList;

}
