package com.sjf.service.produce;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.produce.Task;
import com.sjf.entity.produce.WorkOrder;
import com.sjf.vo.produce.WorkOrderSaveVo;
import com.sjf.vo.produce.WorkerOrderQueryVo;

import java.util.List;

/**
 * @Description: 工单管理 Service 接口
 * @Author: SJF
 * @Date: 2024/2/5 22:06
 */

public interface WorkOrderService extends IService<WorkOrder> {
    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 当前页
     * @param size: 每页显示的条数
     * @param workerOrderQueryVo: 传输的查询条件对象
     */
    Result<Page<WorkOrder>> conditionQuery(Integer current, Integer size, WorkerOrderQueryVo workerOrderQueryVo);

    /**
     * 根据工单主键 id 查询工单对应任务详细信息
     * @param workOrderId : 工单主键 id
     * @return: 工单对应任务详细信息
     */
    Result<List<Task>> getTasksByWorkOrderId(Long workOrderId);

    /**
     * 根据订单 id 创建订单对应的工单
     * @param orderId: 订单 id
     * @param workOrderSaveVoList: 生成工单所需的传输条件
     * @return: 创建订单的结果信息
     */
    Result<Object> saveWorkOrder(Long orderId, List<WorkOrderSaveVo> workOrderSaveVoList);

    /**
     * 校验并修改工单信息。
     * @param workOrder: 工单信息
     * @return: 修改的结果
     */
    Result<Object> updateWorkOrder(WorkOrder workOrder);

    /**
     * 根据工单 id 删除工单信息。
     * @param workOrderId: 工单 id
     * @return: 删除的结果信息
     */
    Result<Object> deleteWorkOrder(Long workOrderId);
}
