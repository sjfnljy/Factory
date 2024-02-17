package com.sjf.entity.produce;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 工单和产品中间表实体类
 * @Author: SJF
 * @Date: 2024/2/7 17:01
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_produce_work_product")
public class WorkOrderAndProduct extends BaseEntity {

    @ApiModelProperty("工单 id")
    private Long workOrderId;

    @ApiModelProperty("产品 id")
    private Long productId;

    public WorkOrderAndProduct(Long workOrderId, Long productId) {
        this.workOrderId = workOrderId;
        this.productId = productId;
    }
}
