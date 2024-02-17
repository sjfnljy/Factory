package com.sjf.vo.produce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 订单条件查询传输类
 * @Author: SJF
 * @Date: 2024/2/4 17:22
 */

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("订单条件查询传输类")
public class OrderQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty("起始时间")
    private Date beginTime;

    @ApiModelProperty("结束时间")
    private Date endTime;
}
