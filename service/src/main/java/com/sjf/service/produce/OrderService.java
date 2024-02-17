package com.sjf.service.produce;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.produce.Order;
import com.sjf.entity.produce.WorkOrder;
import com.sjf.vo.produce.OrderQueryVo;

import java.util.List;

/**
 * @Description: 订单管理 Service 接口
 * @Author: SJF
 * @Date: 2024/1/27 21:06
 */

public interface OrderService extends IService<Order> {
    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 当前页
     * @param size: 每页显示的条数
     * @param orderQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<Order>> conditionQuery(Integer current, Integer size, OrderQueryVo orderQueryVo);

    /**
     * 根据订单主键 id 查询订单对应工单详细信息
     * @param orderId: 订单主键 id
     * @return: 订单对应工单详细信息
     */
    Result<List<WorkOrder>> getWorksByOrderId(Long orderId);

    /**
     * 校验新增的订单对象并完成新增
     * @param order: 传入的新增的订单对象
     * @return: 校验及新增的结果信息
     */
    Result<Object> saveOrder(Order order);

    /**
     * 校验修改的订单对象并完成修改
     * @param order: 传入的修改的订单对象
     * @return: 校验及修改的结果信息
     */
    Result<Object> updateOrder(Order order);

    /**
     * 根据传输的订单 id 删除对象
     * @param orderId: 传入的删除的订单 id
     * @return: 校验及删除的结果信息
     */
    Result<Object> deleteProduct(Long orderId);
}
