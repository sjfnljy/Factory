package com.sjf.entity.produce;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description: 订单实体类
 * @Author: SJF
 * @Date: 2024/1/27 21:01
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_produce_order")
public class Order extends BaseEntity {

    @TableField(value = "order_code")
    @ApiModelProperty("订单编号")
    private String orderCode;

    @TableField(value = "customer_name")
    @ApiModelProperty("客户名称")
    private String customerName;

    @TableField(value = "planned_number")
    @ApiModelProperty("计划数量")
    private Integer plannedNumber;

    @TableField(value = "completed_number")
    @ApiModelProperty("实际完成数量")
    private Integer completedNumber;

    @TableField(value = "planned_time")
    @ApiModelProperty("计划完成时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date plannedTime;

    @TableField(value = "completed_time")
    @ApiModelProperty("实际完成时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date completedTime;
}
