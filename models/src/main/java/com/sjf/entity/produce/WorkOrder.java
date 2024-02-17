package com.sjf.entity.produce;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description: 工单实体类
 * @Author: SJF
 * @Date: 2024/2/4 17:42
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_produce_work_order")
public class WorkOrder extends BaseEntity {

    @TableField("work_order_code")
    @ApiModelProperty("工单编号")
    private String workOrderCode;

    @TableField("status")
    @ApiModelProperty("工单状态")
    private Integer status;

    @TableField("priority")
    @ApiModelProperty("工单优先级")
    private Integer priority;

    @TableField("planned_number")
    @ApiModelProperty("计划数量")
    private Integer plannedNumber;

    @TableField("completed_number")
    @ApiModelProperty("实际完成数量")
    private Integer completedNumber;

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

    @TableField("report_time")
    @ApiModelProperty("报工时长")
    private Date reportTime;
}
