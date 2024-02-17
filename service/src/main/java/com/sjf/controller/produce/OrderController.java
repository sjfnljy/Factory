package com.sjf.controller.produce;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.produce.Order;
import com.sjf.entity.produce.WorkOrder;
import com.sjf.service.produce.OrderService;
import com.sjf.vo.produce.OrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 订单请求处理器
 * @Author: SJF
 * @Date: 2024/1/27 20:59
 */

@Api(tags = "订单管理接口")
@RestController
@RequestMapping("/factory/produce/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @ApiOperation("查询所有订单")
    @GetMapping("/all")
    public Result<List<Order>> getAllOrders(){
        List<Order> orderList = orderService.list();
        if(CollectionUtil.isEmpty(orderList)){
            return Result.build(HttpCode.SUCCESS,"订单查询结果为空");
        }
        return Result.ok(orderList);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<Order>> conditionQuery(@PathVariable("current") Integer current,
                                                @PathVariable("size") Integer size,
                                                OrderQueryVo orderQueryVo) {
        return orderService.conditionQuery(current, size, orderQueryVo);
    }

    @ApiOperation("根据订单主键 id 查询订单对应工单详细信息")
    @GetMapping("/get/{orderId}")
    public Result<List<WorkOrder>> getWorksById(@PathVariable("orderId") Long orderId) {
        return orderService.getWorksByOrderId(orderId);
    }

    @ApiOperation("新增订单")
    @PostMapping("/save")
    public Result<Object> saveOrder(@RequestBody Order order){
        return orderService.saveOrder(order);
    }

    @ApiOperation("修改订单信息")
    @PutMapping("/update")
    public Result<Object> updateOrder(@RequestBody Order order){
        return orderService.updateOrder(order);
    }

    @ApiOperation("根据订单 id 删除订单")
    @DeleteMapping("/delete/{orderId}")
    public Result<Object> deleteOrder(@PathVariable("orderId") Long orderId){
        return orderService.deleteProduct(orderId);
    }

}
