package com.sjf.vo.produce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 工单查询传输类
 * @Author: SJF
 * @Date: 2024/2/5 21:56
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("工单条件查询传输类")
public class WorkerOrderQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单编号")
    private String workOrderCode;

    @ApiModelProperty(value = "工单状态")
    private Integer status;

    @ApiModelProperty(value = "计划结束时间")
    private Date plannedEndTime;
}
