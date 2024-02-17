package com.sjf.entity.produce;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 订单与工单中间表实体类
 * @Author: SJF
 * @Date: 2024/2/5 15:20
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_produce_order_work")
public class OrderAndWorkOrder extends BaseEntity {

    @TableField("order_id")
    private Long orderId;

    @TableField("work_order_id")
    private Long workOrderId;

    public OrderAndWorkOrder(Long orderId, Long workOrderId) {
        this.orderId = orderId;
        this.workOrderId = workOrderId;
    }
}
