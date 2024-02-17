package com.sjf.service.produce.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.constant.ProcessStatus;
import com.sjf.constant.ProducePriority;
import com.sjf.entity.base.Process;
import com.sjf.entity.produce.Task;
import com.sjf.entity.produce.TaskAndProcess;
import com.sjf.entity.produce.WorkOrder;
import com.sjf.entity.produce.WorkOrderAndTask;
import com.sjf.mapper.base.ProcessMapper;
import com.sjf.mapper.produce.TaskAndProcessMapper;
import com.sjf.mapper.produce.TaskMapper;
import com.sjf.mapper.produce.WorkOrderAndTaskMapper;
import com.sjf.mapper.produce.WorkOrderMapper;
import com.sjf.service.produce.TaskService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.produce.TaskQueryVo;
import com.sjf.vo.produce.TaskSaveVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 任务管理 Service 接口实现类
 * @Author: SJF
 * @Date: 2024/2/8 21:20
 */

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private WorkOrderAndTaskMapper workOrderAndTaskMapper;

    @Resource
    private WorkOrderMapper workOrderMapper;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private TaskAndProcessMapper taskAndProcessMapper;


    @Override
    public Result<Page<Task>> conditionQuery(Integer current, Integer size, TaskQueryVo taskQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(taskQueryVo == null || current == null || size == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        } else if(current <= 0 || size <= 0){
            // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }

        // 获取传输的查询条件。
        String taskCode = Objects.requireNonNull(taskQueryVo).getTaskCode();
        Integer status = Objects.requireNonNull(taskQueryVo).getStatus();
        Date plannedEndTime = Objects.requireNonNull(taskQueryVo).getPlannedEndTime();
        Integer badProductNumber = Objects.requireNonNull(taskQueryVo).getBadProductNumber();
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(taskCode), Task::getTaskCode, taskCode)
                .eq(status != null, Task::getStatus, status)
                .ge(badProductNumber != null && badProductNumber > 0, Task::getBadProductNumber, badProductNumber)
                .le(plannedEndTime != null, Task::getPlannedEndTime, plannedEndTime)
                .orderByDesc(Task::getPriority);

        // 执行查询并返回分页结果信息。
        Page<Task> taskPage = taskMapper.selectPage(new Page<>(current, size), wrapper);
        if (taskPage == null) {
            return Result.build(HttpCode.QUERY_FAILURE,"条件查询结果不存在");
        }
        return Result.ok(taskPage);
    }

    @Override
    public Result<Object> saveTask(Long workOrderId, List<TaskSaveVo> taskSaveVoList) {
        // 校验工单 id 对应的工单是否存在。
        if (workOrderId == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的工单 id 为空");
        }
        WorkOrder quiredWorkOrder = workOrderMapper.getWorkOrderById(workOrderId);
        if (quiredWorkOrder == null){
                return Result.build(HttpCode.MODIFICATION_FAILURE, "不存在对应的工单信息");
        }

        // 校验传输的任务创建传输对象集合。
        if (CollUtil.isEmpty(taskSaveVoList)){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的任务创建传输对象集合为空");
        }
        for (TaskSaveVo taskSaveVo : taskSaveVoList) {
            if (taskSaveVo != null){
                Task task = new Task();
                // 校验传输的工序 id。
                Long processId = taskSaveVo.getProcessId();
                if (processId == null){
                    return Result.build(HttpCode.UNPASSED_PARAMETER, "未传输工序信息");
                } else {
                    Process quiredProcess = processMapper.getProcessById(processId);
                    if (quiredProcess == null){
                        return Result.build(HttpCode.ADD_FAILURE, "传输的工序信息有误");
                    }
                }

                // 校验传输任务编号。
                String taskCode = taskSaveVo.getTaskCode();
                if (taskCode == null || StrUtil.isBlank(taskCode)){
                    taskCode = PrefixGenerateUtil.generateCode("RW");
                } else {
                    Task quiredTask = taskMapper.getTaskByCode(taskCode);
                    if (quiredTask != null){
                        return Result.build(HttpCode.ADD_FAILURE, "传入的任务编号已存在");
                    }
                }
                task.setTaskCode(taskCode);

                // 校验传输任务优先级。
                Integer priority = taskSaveVo.getPriority();
                if (priority == null) {
                    return Result.build(HttpCode.ADD_FAILURE, "未传输任务优先级");
                } else if (!ProducePriority.isValidType(priority)) {
                    return Result.build(HttpCode.ADD_FAILURE, "传输的任务优先级信息有误");
                }
                task.setPriority(priority);
                // 校验传输的任务计划开始时间。
                Date plannedBeginTime = taskSaveVo.getPlannedBeginTime();
                if (plannedBeginTime == null) {
                    return Result.build(HttpCode.ADD_FAILURE, "未传输任务计划开始时间");
                } else {
                    if (plannedBeginTime.before(new Date())) {
                        return Result.build(HttpCode.ADD_FAILURE, "传输的任务计划开始时间信息有误");
                    }
                }
                task.setPlannedBeginTime(plannedBeginTime);

                // 校验传输的任务计划结束时间。
                Date plannedEndTime = taskSaveVo.getPlannedEndTime();
                if (plannedEndTime == null) {
                    return Result.build(HttpCode.ADD_FAILURE, "未传输任务计划结束时间");
                } else {
                    if (plannedEndTime.before(new Date()) || plannedEndTime.before(plannedBeginTime)) {
                        return Result.build(HttpCode.ADD_FAILURE, "传输的任务计划结束时间信息有误");
                    }
                }
                task.setPlannedEndTime(plannedEndTime);

                // 设置任务的计划完成数目: 与工单的计划完成数目一致。
                task.setPlannedNumber(quiredWorkOrder.getPlannedNumber());

                // 校验通过则插入数据库中，同时插入工单-任务中间表记录和任务-工序中间表记录。
                taskMapper.insert(task);
                Long taskId = task.getId();
                workOrderAndTaskMapper.insert(new WorkOrderAndTask(workOrderId, taskId));
                taskAndProcessMapper.insert(new TaskAndProcess(taskId, processId));
            }
        }
        return Result.build(HttpCode.SUCCESS, "新增任务信息成功");
    }

    @Override
    public Result<Object> updateTask(Task task) {
        // 校验传输的任务对象。
        if (task == null || task.getId() == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的任务对象为空");
        }
        Task quiredTask = taskMapper.getTaskById(task.getId());
        if (quiredTask == null){
            return Result.build(HttpCode.QUERY_FAILURE, "传入的任务 id 对应的任务不存在");
        }

        // 如果要修改任务编号，则校验任务编号。
        String taskCode = task.getTaskCode();
        if (taskCode != null && StrUtil.isBlank(taskCode) && !taskCode.equals(quiredTask.getTaskCode())){
            Task aTask = taskMapper.getTaskByCode(taskCode);
            if (aTask != null){
                return Result.build(HttpCode.MODIFICATION_FAILURE, "传入的任务编号已存在");
            }
        }

        // 如果要修改任务状态，则校验状态值是否合法。
        Integer status = task.getStatus();
        if (status != null && !ProcessStatus.isValidStatus(status)){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "传入的任务状态信息有误");
        }

        // 如果要修改任务优先级，则校验优先级值是否合法。
        Integer priority = task.getPriority();
        if (priority!= null &&!ProducePriority.isValidType(priority)){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "传入的任务优先级信息有误");
        }

        // 如果要修改任务计划开始时间，则校验任务计划开始时间。
        Date plannedBeginTime = task.getPlannedBeginTime();
        if (plannedBeginTime != null && plannedBeginTime.before(new Date())){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "任务计划开始时间不能晚于当前时间");
        } else {
            Date plannedEndTime = task.getPlannedEndTime();
            if (plannedEndTime!= null && plannedEndTime.before(plannedBeginTime)) {
                return Result.build(HttpCode.MODIFICATION_FAILURE, "任务计划开始时间不能晚于计划结束时间");
            }
        }

        // 如果要修改任务生产的良品数量，则其数量加原始数量不得超过计划生产数目。
        Integer goodProductNumber = task.getGoodProductNumber();
        if (goodProductNumber!= null && (goodProductNumber <= 0 || goodProductNumber > task.getPlannedNumber())){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "任务生产的良品数量不能超过计划生产数目");
        }

        // 校验通过则进行修改。
        int update = taskMapper.updateById(task);
        return Result.build(HttpCode.SUCCESS, "修改" + update + "条任务信息成功");
    }

    @Override
    public Result<Object> deleteTask(Long taskId) {
        // 校验传输的任务 id。
        if (taskId == null) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "未传输任务 id 信息");
        } else {
            Task quiredTask = taskMapper.getTaskById(taskId);
            if (quiredTask == null){
                return Result.build(HttpCode.QUERY_FAILURE, "传入的任务 id 对应的任务不存在");
            }
        }

        // 校验是否还有工单在使用任务，若有则不能删除。
        Long workOrderId = workOrderAndTaskMapper.getWorkOrderIdByTaskId(taskId);
        if (workOrderId != null){
            return Result.build(HttpCode.DELETION_FAILURE, "仍有工单在使用任务，不能删除");
        }

        // 校验通过则删除任务信息，并删除任务-工序中间信息。
        int delete = taskMapper.deleteById(taskId);
        taskAndProcessMapper.deleteTaskProcessByTaskId(taskId);
        return Result.build(HttpCode.SUCCESS, "删除" + delete + "条任务信息成功");
    }
}
