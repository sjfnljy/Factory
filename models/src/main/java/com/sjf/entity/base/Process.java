package com.sjf.entity.base;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 工序实体类
 * @Author: SJF
 * @Date: 2024/1/19 13:46
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_process")
public class Process extends BaseEntity {

    @TableField("process_name")
    @ApiModelProperty("工序名称")
    @ExcelProperty(value = "工序名称",index = 0)
    private String processName;

    @TableField("process_code")
    @ApiModelProperty("工序编号")
    @ExcelProperty(value = "工序编号",index = 1)
    private String processCode;

    @TableField("process_ratio")
    @ApiModelProperty("报工比")
    @ExcelProperty(value = "报工比",index = 2)
    private Double processRatio;

    @TableField("status")
    @ApiModelProperty("工序状态 0：未开始，1：执行中，2：已取消，3：已结束，4：已完成")
    @ExcelProperty(value = "工序状态",index = 3)
    private Integer processStatus;

    @TableField(exist = false)
    @ExcelIgnore
    @ApiModelProperty("该工序对应不良品项 id 集合")
    private List<Long> defectIdList;

}
