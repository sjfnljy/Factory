package com.sjf.entity.produce;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 工单任务管理实体类
 * @Author: SJF
 * @Date: 2024/2/7 14:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_produce_work_task")
public class WorkOrderAndTask extends BaseEntity {

    @TableField("work_order_id")
    @ApiModelProperty(value = "工单 id")
    private Long workOrderId;

    @TableField("task_id")
    @ApiModelProperty(value = "任务 id")
    private Long taskId;

    public WorkOrderAndTask(Long workOrderId, Long taskId) {
        this.workOrderId = workOrderId;
        this.taskId = taskId;
    }
}
