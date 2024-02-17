package com.sjf.vo.produce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 新增任务传输类
 * @Author: SJF
 * @Date: 2024/2/14 17:31
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "创建任务时传输类")
public class TaskSaveVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务所对应的的工序 id")
    private Long processId;

    @ApiModelProperty("任务编号")
    private String taskCode;

    @ApiModelProperty("任务优先级")
    private Integer priority;

    @ApiModelProperty("计划开始时间")
    private Date plannedBeginTime;

    @ApiModelProperty("计划结束时间")
    private Date plannedEndTime;
}
