package com.sjf.entity.produce;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 任务和工序中间表实体类
 * @Author: SJF
 * @Date: 2024/2/14 17:38
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_produce_task_process")
@ApiModel(value = "任务工序中间表实体类")
public class TaskAndProcess extends BaseEntity {

    @TableField("task_id")
    @ApiModelProperty("任务 id")
    private Long taskId;

    @TableField("process_id")
    @ApiModelProperty("工序 id")
    private Long processId;

    public TaskAndProcess(Long taskId, Long processId) {
        this.taskId = taskId;
        this.processId = processId;
    }
}
