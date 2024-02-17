package com.sjf.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.produce.Order;
import com.sjf.entity.produce.OrderAndWorkOrder;
import com.sjf.entity.produce.WorkOrder;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 订单和工单中间表实体类
 * @Author: SJF
 * @Date: 2024/2/5 15:25
 */

@Resource
public interface OrderAndWorkOrderMapper extends BaseMapper<OrderAndWorkOrder> {

    List<WorkOrder> getWorkOrderList(@Param("order_id") Long orderId);

    void deleteOrderAndWork(@Param("order_id") Long orderId);

    Long getOrderIdByWorkOrderId(@Param("work_order_id") Long workOrderId);
}
