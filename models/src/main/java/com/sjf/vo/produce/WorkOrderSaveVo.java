package com.sjf.vo.produce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 创建工单时条件传输类
 * @Author: SJF
 * @Date: 2024/2/7 15:27
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("创建工单时传输类")
public class WorkOrderSaveVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("工单生产的产品 id")
    private Long productId;

    @ApiModelProperty("工单编号")
    private String workerOrderCode;

    @ApiModelProperty("工单优先级")
    private Integer priority;

    @ApiModelProperty("计划生产数目")
    private Integer plannedNumber;

    @ApiModelProperty("计划开始时间")
    private Date plannedBeginTime;

    @ApiModelProperty("计划结束时间")
    private Date plannedEndTime;

}
