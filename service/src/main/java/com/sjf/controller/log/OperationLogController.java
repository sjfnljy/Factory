package com.sjf.controller.log;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.common.log.OperationLogService;
import com.sjf.entity.log.OperationLog;
import com.sjf.vo.log.OperationLogQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description: 操作日志请求处理器
 * @Author: SJF
 * @Date: 2024/1/27 19:49
 */

@Api(tags = "操作日志管理接口")
@RestController
@RequestMapping("/factory/log/operate")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<OperationLog>> conditionQuery(@PathVariable("current") Integer current,
                                                     @PathVariable("size") Integer size,
                                                     OperationLogQueryVo operationLogQueryVo){
        return operationLogService.conditionQuery(current, size, operationLogQueryVo);
    }

    @ApiOperation("根据日志 id 删除日志")
    @DeleteMapping("/delete/{operationId}")
    public Result<Object> deleteOperationLog(@PathVariable("operationId") Long operationId){
        return operationLogService.deleteOperationLog(operationId);
    }

}
