package com.sjf.entity.base;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 不良品项类型实体类
 * @Author: SJF
 * @Date: 2024/1/19
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_defect")
public class Defect extends BaseEntity {

    @TableField("defect_code")
    @ApiModelProperty("不良品项编号")
    @ExcelProperty(value = "不良品项编号",index = 0)
    private String defectCode;

    @TableField("defect_name")
    @ApiModelProperty("不良品项名称")
    @ExcelProperty(value = "不良品项名称",index = 1)
    private String defectName;

    @TableField("quantity")
    @ApiModelProperty("不良品项数量")
    @ExcelProperty(value = "不良品项数量",index = 2)
    private Integer quantity;

    @TableField("defect_cause")
    @ApiModelProperty("不良品项原因")
    @ExcelProperty(value = "不良品项原因",index = 3)
    private String defectCause;
}
