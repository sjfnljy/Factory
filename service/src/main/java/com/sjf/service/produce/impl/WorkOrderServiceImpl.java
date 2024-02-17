package com.sjf.service.produce.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.constant.ProducePriority;
import com.sjf.constant.ProduceStatus;
import com.sjf.entity.base.Product;
import com.sjf.entity.produce.*;
import com.sjf.mapper.base.ProductMapper;
import com.sjf.mapper.produce.*;
import com.sjf.service.produce.WorkOrderService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.produce.WorkOrderSaveVo;
import com.sjf.vo.produce.WorkerOrderQueryVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 工单管理 Service 接口实现类
 * @Author: SJF
 * @Date: 2024/2/5 22:06
 */

@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private WorkOrderMapper workOrderMapper;

    @Resource
    private OrderAndWorkOrderMapper orderAndWorkOrderMapper;

    @Resource
    private WorkOrderAndTaskMapper workOrderAndTaskMapper;

    @Resource
    private WorkOrderAndProductMapper workOrderAndProductMapper;

    @Override
    public Result<Page<WorkOrder>> conditionQuery(Integer current, Integer size, WorkerOrderQueryVo workerOrderQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(workerOrderQueryVo == null || current == null || size == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        } else if(current <= 0 || size <= 0){
            // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }

        // 获取传输的查询条件。
        String workOrderCode = Objects.requireNonNull(workerOrderQueryVo).getWorkOrderCode();
        Integer status = Objects.requireNonNull(workerOrderQueryVo).getStatus();
        Date plannedEndTime = Objects.requireNonNull(workerOrderQueryVo).getPlannedEndTime();
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(workOrderCode), WorkOrder::getWorkOrderCode, workOrderCode)
                .eq(status != null, WorkOrder::getStatus, status)
                .le(plannedEndTime != null, WorkOrder::getPlannedEndTime, plannedEndTime)
                // 根据优先级降序排序。
                .orderByDesc(WorkOrder::getPriority);
        Page<WorkOrder> workOrderPage = workOrderMapper.selectPage(new Page<>(current, size), wrapper);
        if (workOrderPage == null){
            return Result.build(HttpCode.QUERY_FAILURE,"条件查询结果不存在");
        }
        return Result.ok(workOrderPage);
    }

    @Override
    public Result<List<Task>> getTasksByWorkOrderId(Long workOrderId) {
        // 校验传输的工单 id 信息。
        if (workOrderId == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工单 id 为空");
        } else {
            WorkOrder quiredWorkOrder =  workOrderMapper.getWorkOrderById(workOrderId);
            if (quiredWorkOrder == null){
                return Result.build(HttpCode.QUERY_FAILURE, "传入的工单 id 对应的工单不存在");
            }
        }

        // 工单存在则查询对应任务信息。
        List<Task> taskList = workOrderAndTaskMapper.getTasksByWorkOrderId(workOrderId);
        return Result.ok(taskList);
    }

    @Override
    public Result<Object> saveWorkOrder(Long orderId, List<WorkOrderSaveVo> workOrderSaveVoList) {
        // 校验订单 id 对应的订单是否存在。
        if (orderId == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的订单 id 为空");
        } else {
            Order quiredOrder =  orderMapper.getOrderById(orderId);
            if (quiredOrder == null){
                return Result.build(HttpCode.QUERY_FAILURE, "传入的订单 id 对应的订单不存在");
            }
        }

        // 校验传输的订单创建传输对象集合。
        if (CollUtil.isEmpty(workOrderSaveVoList)){
            return Result.build(HttpCode.ADD_FAILURE, "未传输创建工单所需的条件");
        } else {
            for (WorkOrderSaveVo workOrderSaveVo : workOrderSaveVoList){
                if (workOrderSaveVo != null){
                    WorkOrder workOrder = new WorkOrder();
                    // 校验传输的产品 id。
                    Long productId = workOrderSaveVo.getProductId();
                    if (productId == null) {
                        return Result.build(HttpCode.ADD_FAILURE, "传输的产品信息有误");
                    } else{
                        Product productById = productMapper.getProductById(productId);
                        if (productById == null) {
                            return Result.build(HttpCode.ADD_FAILURE, "传输的产品 id 对应的产品不存在, 其 id 为" + productId);
                        }
                    }
                    // 校验传输的工单编号。
                    String workerOrderCode = workOrderSaveVo.getWorkerOrderCode();
                    if (workerOrderCode == null || StrUtil.isBlank(workerOrderCode)){
                        workerOrderCode = PrefixGenerateUtil.generateCode("GD");
                    } else {
                        WorkOrder quiredWorkOrder = workOrderMapper.getWorkOrderByCode(workerOrderCode);
                        if (quiredWorkOrder != null) {
                            return Result.build(HttpCode.ADD_FAILURE, "工单编号对应的工单已存在");
                        }
                    }
                    workOrder.setWorkOrderCode(workerOrderCode);
                    // 校验传输的工单优先级。
                    Integer priority = workOrderSaveVo.getPriority();
                    if (priority == null) {
                        return Result.build(HttpCode.ADD_FAILURE, "未传输工单优先级");
                    } else if (!ProducePriority.isValidType(priority)) {
                        return Result.build(HttpCode.ADD_FAILURE, "传输的工单优先级信息有误");
                    }
                    workOrder.setPriority(priority);
                    // 校验传输的工单计划开始时间。
                    Date plannedBeginTime = workOrderSaveVo.getPlannedBeginTime();
                    if (plannedBeginTime == null) {
                        return Result.build(HttpCode.ADD_FAILURE, "未传输工单计划开始时间");
                    } else {
                        if (plannedBeginTime.before(new Date())) {
                            return Result.build(HttpCode.ADD_FAILURE, "传输的工单计划开始时间信息有误");
                        }
                    }
                    workOrder.setPlannedBeginTime(plannedBeginTime);
                    // 校验传输的工单计划结束时间。
                    Date plannedEndTime = workOrderSaveVo.getPlannedEndTime();
                    if (plannedEndTime == null) {
                        return Result.build(HttpCode.ADD_FAILURE, "未传输工单计划结束时间");
                    } else {
                        if (plannedEndTime.before(new Date()) || plannedEndTime.before(plannedBeginTime)) {
                            return Result.build(HttpCode.ADD_FAILURE, "传输的工单计划结束时间信息有误");
                        }
                    }
                    workOrder.setPlannedEndTime(plannedEndTime);
                    // 校验传输的工单计划生产数目。
                    Integer plannedNumber = workOrderSaveVo.getPlannedNumber();
                    if (plannedNumber == null){
                        return Result.build(HttpCode.ADD_FAILURE, "未传输工单计划生产数目");
                    } else if (plannedNumber <= 0){
                        return Result.build(HttpCode.ADD_FAILURE, "传输的工单计划生产数目不能小于 0");
                    }
                    workOrder.setPlannedNumber(plannedNumber);

                    // 校验通过则插入数据库中，同时插入订单-工单中间表记录和工单-产品中间表记录。
                    int insert = workOrderMapper.insert(workOrder);
                    Long workOrderId = workOrder.getId();
                    orderAndWorkOrderMapper.insert(new OrderAndWorkOrder(orderId, workOrderId));
                    workOrderAndProductMapper.insert(new WorkOrderAndProduct(workOrderId, productId));
                }
            }
        }
        return Result.build(HttpCode.SUCCESS, "新增工单信息成功");
    }

    @Override
    public Result<Object> updateWorkOrder(WorkOrder workOrder) {
        // 校验传输的订单对象。
        if (workOrder == null || workOrder.getId() == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "未传输工单信息");
        }
        WorkOrder quiredWorkerOrder = workOrderMapper.getWorkOrderById(workOrder.getId());
        if (quiredWorkerOrder == null){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "传入的工单 id 对应的工单不存在");
        }

        // 如果要修改工单编号，则校验工单编号。
        String workOrderCode = workOrder.getWorkOrderCode();
        if (workOrderCode != null && StrUtil.isNotBlank(workOrderCode) && !workOrderCode.equals(quiredWorkerOrder.getWorkOrderCode())){
            WorkOrder aWorkOrder = workOrderMapper.getWorkOrderByCode(workOrderCode);
            if (aWorkOrder != null){
                return Result.build(HttpCode.MODIFICATION_FAILURE, "工单编号对应的工单已存在");
            }
        }
        // 如果要修改工单状态，则校验状态值是否合法。
        Integer status = workOrder.getStatus();
        if (status != null && !ProduceStatus.isValidStatus(status)){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "传输的工单状态信息有误");
        }
        // 如果要修改工单优先级，则校验优先级值。
        Integer priority = workOrder.getPriority();
        if (priority!= null &&!ProducePriority.isValidType(priority)){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "传输的工单优先级信息有误");
        }
        // 如果要修改计划开始时间和计划完成时间，则校验计划开始时间和计划完成时间。
        Date plannedBeginTime = workOrder.getPlannedBeginTime();
        if (plannedBeginTime != null && plannedBeginTime.before(new Date())){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "计划开始时间不能晚于当前时间");
        } else {
            Date plannedEndTime = workOrder.getPlannedEndTime();
            if (plannedEndTime!= null && plannedEndTime.before(plannedBeginTime)) {
                return Result.build(HttpCode.MODIFICATION_FAILURE, "计划开始时间不能晚于计划结束时间");
            }
        }
        // 校验通过则进行修改。
        int update = workOrderMapper.updateById(workOrder);
        return Result.build(HttpCode.SUCCESS, "修改" + update + "条工单信息成功");
    }

    @Override
    public Result<Object> deleteWorkOrder(Long workOrderId) {
        // 校验传输的工单 id。
        if (workOrderId == null) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "未传输工单 id 信息");
        } else {
            WorkOrder quiredWorkOrder = workOrderMapper.getWorkOrderById(workOrderId);
            if (quiredWorkOrder == null){
                return Result.build(HttpCode.DELETION_FAILURE, "不存在对应的工单信息");
            }
        }
        // 校验是否还有订单在使用工单，若有则无法删除。
        Long orderId = orderAndWorkOrderMapper.getOrderIdByWorkOrderId(workOrderId);
        if (orderId != null){
            return Result.build(HttpCode.DELETION_FAILURE, "还有订单在使用工单，无法删除");
        }

        // 校验通过则删除工单信息，并删除工单所有任务以及工单产品信息。
        int delete = workOrderMapper.deleteById(workOrderId);
        workOrderAndTaskMapper.deleteWorkOrderAndTask(workOrderId);
        workOrderAndProductMapper.deleteWorkOrderAndProduct(workOrderId);
        return Result.build(HttpCode.SUCCESS, "删除" + delete + "条工单信息成功");
    }


}
