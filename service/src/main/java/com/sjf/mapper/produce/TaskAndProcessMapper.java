package com.sjf.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.produce.TaskAndProcess;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 任务工序中间表 Mapper 接口
 * @Author: SJF
 * @Date: 2024/2/14 17:42
 */

@Repository
public interface TaskAndProcessMapper extends BaseMapper<TaskAndProcess> {

    void deleteTaskProcessByTaskId(@Param("task_id") Long taskId);

    List<Long> getTaskIdByProcessId(@Param("process_id") Long processId);
}
