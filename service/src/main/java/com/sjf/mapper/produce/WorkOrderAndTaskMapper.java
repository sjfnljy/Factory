package com.sjf.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.produce.Task;
import com.sjf.entity.produce.WorkOrderAndTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 工单任务管理 Mapper 接口
 * @Author: SJF
 * @Date: 2024/2/7 14:52
 */

@Repository
public interface WorkOrderAndTaskMapper extends BaseMapper<WorkOrderAndTask> {
    List<Task> getTasksByWorkOrderId(@Param("work_order_id") Long workOrderId);

    void deleteWorkOrderAndTask(@Param("work_order_id")Long workOrderId);

    Long getWorkOrderIdByTaskId(@Param("task_id") Long taskId);
}
