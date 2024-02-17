package com.sjf.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.produce.Task;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 任务管理 Mapper 接口
 * @Author: SJF
 * @Date: 2024/2/7 14:41
 */

@Repository
public interface TaskMapper extends BaseMapper<Task> {
    Task getTaskByCode(@Param("task_code") String taskCode);

    Task getTaskById(@Param("task_id") Long id);
}
