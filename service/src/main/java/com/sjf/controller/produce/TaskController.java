package com.sjf.controller.produce;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.produce.Task;
import com.sjf.service.produce.TaskService;
import com.sjf.vo.produce.TaskQueryVo;
import com.sjf.vo.produce.TaskSaveVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 任务请求处理器
 * @Author: SJF
 * @Date: 2024/2/8 21:19
 */

@Api(tags = "工单管理接口")
@RestController
@RequestMapping("/factory/produce/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    @ApiOperation("查询所有任务")
    @GetMapping("/all")
    public Result<List<Task>> getAllWorkOrders(){
        List<Task> taskList = taskService.list();
        if(CollectionUtil.isEmpty(taskList)){
            return Result.build(HttpCode.SUCCESS,"任务查询结果为空");
        }
        return Result.ok(taskList);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<Task>> conditionQuery(@PathVariable("current") Integer current,
                                             @PathVariable("size") Integer size,
                                             TaskQueryVo taskQueryVo){
        return taskService.conditionQuery(current, size, taskQueryVo);
    }

    @ApiOperation("新增任务")
    @PostMapping("/save/{workOrderId}")
    public Result<Object> saveTask(@PathVariable("workOrderId") Long workOrderId,
                                   @RequestBody List<TaskSaveVo> taskSaveVoList){
        return taskService.saveTask(workOrderId, taskSaveVoList);
    }

    @ApiOperation("修改任务信息")
    @PutMapping("/update")
    public Result<Object> updateTask(@RequestBody Task task) {
        return taskService.updateTask(task);
    }

    @ApiOperation("根据任务 id 删除任务")
    @DeleteMapping("/delete/{taskId}")
    public Result<Object> deleteWorkOrder(@PathVariable("taskId") Long taskId) {
        return taskService.deleteTask(taskId);
    }

    
}
