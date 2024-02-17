package com.sjf.service.produce;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.produce.Task;
import com.sjf.vo.produce.TaskQueryVo;
import com.sjf.vo.produce.TaskSaveVo;

import java.util.List;

/**
 * @Description: 任务管理 Service 接口
 * @Author: SJF
 * @Date: 2024/2/8 21:20
 */

public interface TaskService extends IService<Task> {
    /*
      根据传输的查询条件进行条件分页查询
      @param current: 当前页
     * @param size: 每页显示的条数
     * @param taskQueryVo: 传输的查询条件对象
     **/
    Result<Page<Task>> conditionQuery(Integer current, Integer size, TaskQueryVo taskQueryVo);

    /**
     * 根据工单 id 创建工单对应的任务
     * @param workOrderId: 工单 id
     * @param taskSaveVoList: 生成任务所需的传输条件
     * @return: 创建工单的结果信息
     */
    Result<Object> saveTask(Long workOrderId, List<TaskSaveVo> taskSaveVoList);

    /**
     * 校验并修改任务信息。
     * @param task: 任务信息
     * @return: 修改的结果
     */
    Result<Object> updateTask(Task task);

    /**
     * 根据任务 id 删除任务信息。
     * @param taskId: 任务 id
     * @return: 删除的结果信息
     */
    Result<Object> deleteTask(Long taskId);
}
