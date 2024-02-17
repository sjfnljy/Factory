package com.sjf.controller.produce;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.produce.Task;
import com.sjf.entity.produce.WorkOrder;
import com.sjf.service.produce.WorkOrderService;
import com.sjf.vo.produce.WorkOrderSaveVo;
import com.sjf.vo.produce.WorkerOrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 工单请求处理器
 * @Author: SJF
 * @Date: 2024/2/5 22:05
 */

@Api(tags = "工单管理接口")
@RestController
@RequestMapping("/factory/produce/work")
public class WorkOrderController {
    @Resource
    private WorkOrderService workOrderService;

    @ApiOperation("查询所有工单")
    @GetMapping("/all")
    public Result<List<WorkOrder>> getAllWorkOrders(){
        List<WorkOrder> workOrderList = workOrderService.list();
        if(CollectionUtil.isEmpty(workOrderList)){
            return Result.build(HttpCode.SUCCESS,"工单查询结果为空");
        }
        return Result.ok(workOrderList);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<WorkOrder>> conditionQuery(@PathVariable("current") Integer current,
                                                  @PathVariable("size") Integer size,
                                                  WorkerOrderQueryVo workerOrderQueryVo){
        return workOrderService.conditionQuery(current, size, workerOrderQueryVo);
    }

    @ApiOperation("根据工单主键 id 查询工单对应任务详细信息")
    @GetMapping("/get/{workId}")
    public Result<List<Task>> getTasksById(@PathVariable("workId") Long workOrderId) {
        return workOrderService.getTasksByWorkOrderId(workOrderId);
    }

    @ApiOperation("新增工单")
    @PostMapping("/save/{orderId}")
    public Result<Object> saveWorkOrder(@PathVariable("orderId") Long orderId,
                                    @RequestBody List<WorkOrderSaveVo> workOrderSaveVoList){
        return workOrderService.saveWorkOrder(orderId, workOrderSaveVoList);
    }

    @ApiOperation("修改工单信息")
    @PutMapping("/update")
    public Result<Object> updateWorkOrder(@RequestBody WorkOrder workOrder) {
        return workOrderService.updateWorkOrder(workOrder);
    }

    @ApiOperation("根据工单 id 删除工单")
    @DeleteMapping("/delete/{workId}")
    public Result<Object> deleteWorkOrder(@PathVariable("workId") Long workOrderId) {
        return workOrderService.deleteWorkOrder(workOrderId);
    }

}
