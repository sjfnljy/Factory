package com.sjf.vo.produce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 任务条件查询传输类
 * @Author: SJF
 * @Date: 2024/2/14 17:07
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("任务条件查询传输类")
public class TaskQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务编号")
    private String taskCode;

    @ApiModelProperty(value = "任务状态")
    private Integer status;

    @ApiModelProperty(value = "计划结束时间")
    private Date plannedEndTime;

    @ApiModelProperty(value = "不良品数量")
    private Integer badProductNumber;
}
