package com.sjf.service.produce.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.produce.Order;
import com.sjf.entity.produce.WorkOrder;
import com.sjf.mapper.produce.OrderAndWorkOrderMapper;
import com.sjf.mapper.produce.OrderMapper;
import com.sjf.service.produce.OrderService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.produce.OrderQueryVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 订单管理 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/27 21:07
 */

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderAndWorkOrderMapper orderAndWorkOrderMapper;


    @Override
    public Result<Page<Order>> conditionQuery(Integer current, Integer size, OrderQueryVo orderQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(orderQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))){
            Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        }
        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if(current <= 0 || size <= 0){
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }
        Page<Order> page = new Page<>(current, size);

        // 获取传输的查询条件。
        String orderCode = Objects.requireNonNull(orderQueryVo).getOrderCode();
        String customerName = Objects.requireNonNull(orderQueryVo).getCustomerName();
        Date beginTime = Objects.requireNonNull(orderQueryVo).getBeginTime();
        Date endTime = Objects.requireNonNull(orderQueryVo).getEndTime();
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(orderCode), Order::getOrderCode, orderCode)
                .like(StrUtil.isNotBlank(customerName), Order::getCustomerName, orderCode)
                .between(beginTime != null && endTime != null && beginTime.compareTo(endTime) < 0, Order::getCompletedTime, beginTime, endTime)
                .orderByDesc(Order::getPlannedTime);

        Page<Order> orderPage = orderMapper.selectPage(page, wrapper);
        if (orderPage == null){
            return Result.build(HttpCode.QUERY_FAILURE, "查询失败");
        }
        return Result.ok(orderPage);
    }

    @Override
    public Result<List<WorkOrder>> getWorksByOrderId(Long orderId) {
        if(orderId == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的订单 id 为空");
        }
        // 校验订单 id 对应的订单是否存在。
        Order quiredOrder = orderMapper.getOrderById(orderId);
        if (quiredOrder == null){
            return Result.build(HttpCode.QUERY_FAILURE, "不存在订单 id 对应的订单信息");
        }
        // 订单存在则查询对应的工单信息.
        List<WorkOrder> workOrderList = orderAndWorkOrderMapper.getWorkOrderList(orderId);
        return Result.ok(workOrderList);
    }

    @Override
    public Result<Object> saveOrder(Order order) {
        // 校验传输的订单信息。
        if (order == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "未传输订单信息");
        }
        // 校验传输的订单编号。
        String orderCode = order.getOrderCode();
        if (StrUtil.isBlank(orderCode)){
            String generateCode = PrefixGenerateUtil.generateCode("DD");
            order.setOrderCode(generateCode);
        } else {
            Order quiredOrder = orderMapper.getOrderByCode(orderCode);
            if (quiredOrder != null){
                return Result.build(HttpCode.ADD_FAILURE, "已存在订单编号对应的订单");
            }
        }
        // 校验传输的客户名称。
        String customerName = order.getCustomerName();
        if (StrUtil.isBlank(customerName)){
            return Result.build(HttpCode.ADD_FAILURE, "未传输客户名称");
        }
        // 校验传输的计划数目。
        Integer plannedNumber = order.getPlannedNumber();
        if (plannedNumber == null){
            return Result.build(HttpCode.ADD_FAILURE, "未传输计划完成数目");
        } else if (plannedNumber <= 0){
            return Result.build(HttpCode.INVALID_PARAMETER, "传输的订单计划数目不能小于 0");
        }
        // 校验传输的计划完成时间。
        Date plannedTime = order.getPlannedTime();
        if (plannedTime == null){
            return Result.build(HttpCode.ADD_FAILURE, "未传输订单计划完成时间");
        }

        // 校验完成则新增。
        int insert = orderMapper.insert(order);
        return Result.build(HttpCode.SUCCESS, "新增" + insert + "条订单信息成功");
    }

    @Override
    public Result<Object> updateOrder(Order order) {
        // 校验传输的订单是否存在。
        if (order == null || order.getId() == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "未传输订单信息");
        }
        Order quiredOrder = orderMapper.getOrderById(order.getId());
        if (quiredOrder == null){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "不存在对应的订单");
        }

        // 如果要修改订单编号，则校验修改后的订单编号是否存在。
        String orderCode = order.getOrderCode();
        if (orderCode != null && StrUtil.isNotBlank(orderCode) && !orderCode.equals(quiredOrder.getOrderCode())){
            Order anOrder = orderMapper.getOrderByCode(orderCode);
            if (anOrder != null){
                return Result.build(HttpCode.MODIFICATION_FAILURE, "已存在订单编号对应的订单");
            }
        }

        // 如果要修改订单计划完成时间和实际完成时间，则校验订单计划完成时间。
        Date plannedTime = order.getPlannedTime();
        if (plannedTime != null && plannedTime.before(new Date())){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "订单计划开始时间不能晚于当前时间");
        } else {
            Date completedTime = order.getCompletedTime();
            if (completedTime!= null && completedTime.before(plannedTime)) {
                return Result.build(HttpCode.MODIFICATION_FAILURE, "订单计划开始时间不能晚于实际完成时间");
            }
        }

        // 校验通过则进行修改。
        int update = orderMapper.updateById(order);
        return Result.build(HttpCode.SUCCESS, "修改" + update + "条订单信息成功");
    }

    @Override
    public Result<Object> deleteProduct(Long orderId) {
        // 校验传输的订单 id。
        if (orderId == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "未传输订单 id");
        } else{
            Order quiredOrder = orderMapper.getOrderById(orderId);
            if (quiredOrder == null){
                return Result.build(HttpCode.MODIFICATION_FAILURE, "不存在对应的订单");
            }
        }

        // 校验通过则进行删除。
        int delete = orderMapper.deleteOrder(orderId);
        orderAndWorkOrderMapper.deleteOrderAndWork(orderId);
        return Result.build(HttpCode.SUCCESS, "删除" + delete + "条订单信息成功");
    }


}
