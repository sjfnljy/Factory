package com.sjf.common.log;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.log.OperationLog;
import com.sjf.vo.log.OperationLogQueryVo;
import org.springframework.stereotype.Repository;

/**
 * @Description: 操作日志 Service 接口
 * @Author: SJF
 * @Date: 2024/1/27 19:51
 */

public interface OperationLogService extends IService<OperationLog> {
    /**
     * 条件查询日志记录
     * @param current: 当前页
     * @param size: 每页显示条数
     * @param operationLogQueryVo: 查询条件
     */
    Result<Page<OperationLog>> conditionQuery(Integer current, Integer size, OperationLogQueryVo operationLogQueryVo);

    /**
     * 保存日志记录
     * @param operationLog: 待保存的操作日志
     */
    void saveSysOperLog(OperationLog operationLog) ;

    /**
     * 根据传输的操作日志 id 删除日志信息
     * @param operationId: 操作日志 id
     * @return: 删除的结果信息
     */
    Result<Object> deleteOperationLog(Long operationId);
}
