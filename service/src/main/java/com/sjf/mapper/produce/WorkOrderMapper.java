package com.sjf.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.produce.WorkOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 工单管理实体类
 * @Author: SJF
 * @Date: 2024/2/5 15:31
 */

@Repository
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
    WorkOrder getWorkOrderById(@Param("work_order_id") Long workOrderId);

    WorkOrder getWorkOrderByCode(@Param("work_order_code") String workOrderCode);
}
