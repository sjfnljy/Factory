package com.sjf.entity.produce;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description: 任务实体类
 * @Author: SJF
 * @Date: 2024/2/7 14:39
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_produce_task")
public class Task extends BaseEntity {
    @TableField("task_code")
    @ApiModelProperty("任务编号")
    private String taskCode;

    @TableField("status")
    @ApiModelProperty("任务状态")
    private Integer status;

    @TableField("priority")
    @ApiModelProperty("任务优先级")
    private Integer priority;

    @TableField("planned_begin_time")
    @ApiModelProperty("计划开始时间")
    private Date plannedBeginTime;

    @TableField("planned_end_time")
    @ApiModelProperty("计划结束时间")
    private Date plannedEndTime;

    @TableField("really_begin_time")
    @ApiModelProperty("实际开始时间")
    private Date reallyBeginTime;

    @TableField("really_end_time")
    @ApiModelProperty("实际结束时间")
    private Date reallyEndTime;

    @TableField("planned_number")
    @ApiModelProperty("计划数量")
    private Integer plannedNumber;

    @TableField("good_product_number")
    @ApiModelProperty("良品数量")
    private Integer goodProductNumber;

    @TableField("bad_product_number")
    @ApiModelProperty("不良品数量")
    private Integer badProductNumber;
}
