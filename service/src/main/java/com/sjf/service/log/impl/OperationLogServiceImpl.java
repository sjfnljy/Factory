package com.sjf.service.log.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.log.OperationLog;
import com.sjf.mapper.log.SysOperLogMapper;
import com.sjf.common.log.OperationLogService;
import com.sjf.vo.log.OperationLogQueryVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Description: 操作日志管理 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/27 19:52
 */

@Service
public class OperationLogServiceImpl extends ServiceImpl<SysOperLogMapper, OperationLog> implements OperationLogService {

    @Resource
    private SysOperLogMapper sysOperLogMapper;

    @Override
    public Result<Page<OperationLog>> conditionQuery(Integer current, Integer size, OperationLogQueryVo operationLogQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(operationLogQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))){
            Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        }
        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if(current <= 0 || size <= 0){
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }

        // 获取传输的查询条件。
        String title = Objects.requireNonNull(operationLogQueryVo).getTitle();
        String method = Objects.requireNonNull(operationLogQueryVo).getMethod();
        String requestMethod = Objects.requireNonNull(operationLogQueryVo).getRequestMethod();
        String operationName = Objects.requireNonNull(operationLogQueryVo).getOperationName();
        String operatorType = Objects.requireNonNull(operationLogQueryVo).getOperatorType();
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(title), OperationLog::getTitle, title)
                .eq(StrUtil.isNotBlank(method), OperationLog::getMethod, method)
                .eq(StrUtil.isNotBlank(requestMethod), OperationLog::getRequestMethod, requestMethod)
                .eq(StrUtil.isNotBlank(operationName), OperationLog::getOperationName, operationName)
                .eq(StrUtil.isNotBlank(operatorType), OperationLog::getOperatorType, operatorType)
                .orderByDesc(OperationLog::getUpdateTime);
        Page<OperationLog> operationLogPage = sysOperLogMapper.selectPage(new Page<>(current, size), wrapper);
        if (operationLogPage == null){
            return Result.build(HttpCode.SUCCESS,"查询结果为空");
        }
        return Result.ok(operationLogPage);
    }

    @Override
    public void saveSysOperLog(OperationLog operationLog) {
        sysOperLogMapper.insert(operationLog);
    }

    @Override
    public Result<Object> deleteOperationLog(Long operationId) {
        // 校验传入的日志 id。
        if (operationId == null || StrUtil.isBlank(String.valueOf(operationId))){
            return Result.build(HttpCode.FAILURE, "未传入日志 id");
        }else {
            OperationLog operationLog = sysOperLogMapper.selectById(operationId);
            if (operationLog == null){
                return Result.build(HttpCode.DELETION_FAILURE, "不存在对应的日志信息");
            }
        }

        // 校验通过则进行删除。
        sysOperLogMapper.deleteById(operationId);
        return Result.build(HttpCode.SUCCESS,"删除日志成功");
    }
}
