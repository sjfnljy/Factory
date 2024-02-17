package com.sjf.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.produce.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 订单管理 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/27 21:01
 */

@Repository
public interface OrderMapper extends BaseMapper<Order> {
    Order getOrderById(@Param("order_id") Long orderId);

    Order getOrderByCode(@Param("order_code") String orderCode);

    int deleteOrder(@Param("order_id") Long orderId);
}
