package com.sjf.entity.base;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @Description: 设备维修记录实体类
 * @Author: SJF
 * @Date: 2024/1/19
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_repair_record")
public class RepairRecord extends BaseEntity {

    @TableField("repair_code")
    @ApiModelProperty(value = "维修记录编号")
    @ExcelProperty(value = "维修记录编号", index = 0)
    private String repairCode;

    @TableField("fault_cause")
    @ApiModelProperty(value = "故障原因")
    @ExcelProperty(value = "故障原因", index = 1)
    private String faultCause;

    @TableField("repair_person_name")
    @ApiModelProperty(value = "维修人姓名")
    @ExcelProperty(value = "维修人姓名", index = 2)
    private String repairPersonName;

    @TableField("repair_person_phone")
    @ApiModelProperty(value = "维修人电话")
    @ExcelProperty(value = "维修人电话", index = 3)
    private String repairPersonPhone;

    @TableField("repair_cost")
    @ApiModelProperty(value = "维修费用")
    @ExcelProperty(value = "维修费用", index = 4)
    private BigDecimal repairCost;

    @TableField("repair_result")
    @ApiModelProperty(value = "维修结果")
    @ExcelProperty(value = "维修结果", index = 5)
    private String repairResult;
}
