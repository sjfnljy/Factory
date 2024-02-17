package com.sjf.service.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.component.ExcelListener;
import com.sjf.constant.HttpCode;
import com.sjf.constant.ProcessStatus;
import com.sjf.entity.base.Defect;
import com.sjf.entity.base.Process;
import com.sjf.exception.CustomException;
import com.sjf.mapper.base.ProcessAndDefectMapper;
import com.sjf.mapper.base.ProcessMapper;
import com.sjf.mapper.produce.TaskAndProcessMapper;
import com.sjf.service.base.ProcessService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.base.ProcessQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 工序管理 Service 接口实现类
 * @Author: SJF
 * @Date: 2024/1/23 17:30
 */

@Service
@Slf4j
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessAndDefectMapper processAndDefectMapper;

    @Resource
    private TaskAndProcessMapper taskAndProcessMapper;

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里 URLEncoder.encode 可以防止中文乱码
            String fileName = URLEncoder.encode("工序数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 查询数据库中的数据。
            List<Process> processList = processMapper.selectList(null);
            // 写出数据到浏览器端。
            EasyExcel.write(response.getOutputStream(), Process.class).sheet("工序数据").doWrite(processList);
        }catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public Result<Object> importData(MultipartFile file) {
        ExcelListener<Process> listener = new ExcelListener<>(processMapper);
        // 调用 read 方法读取 excel 数据。
        try {
            EasyExcel.read(file.getInputStream(),
                    Process.class,
                    listener).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(HttpCode.FILE_IMPORT_FAILURE,"导入 Excel 数据失败");
        }
        return Result.build(HttpCode.SUCCESS, "导入 Excel 数据成功");
    }


    @Override
    public Result<Page<Process>> conditionQuery(Integer current, Integer size, ProcessQueryVo processQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(processQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))){
            Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        }
        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if(current <= 0 || size <= 0){
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }
        Page<Process> page = new Page<>(current, size);

        // 获取传输的查询条件。
        String processName = Objects.requireNonNull(processQueryVo).getProcessName();
        String processCode = Objects.requireNonNull(processQueryVo).getProcessCode();
        Integer processRatio = Objects.requireNonNull(processQueryVo).getProcessRatio();
        Integer processStatus = Objects.requireNonNull(processQueryVo).getProcessStatus();
        LambdaQueryWrapper<Process> wrapper = new LambdaQueryWrapper<>();
        // 工序名称模糊查询，工序编号、状态匹配查询，工序报工数配比范围查询。
        wrapper.like(StrUtil.isNotBlank(processName),Process::getProcessName,processName)
                .eq(StrUtil.isNotBlank(processCode),Process::getProcessCode,processCode)
                .eq(processStatus != null && StrUtil.isNotBlank(String.valueOf(processStatus)),Process::getProcessStatus,processStatus)
                .le(processRatio != null && StrUtil.isNotBlank(String.valueOf(processRatio)), Process::getProcessRatio, processRatio);
        // 根据工序修改时间排序。
        wrapper.orderByDesc(Process::getUpdateTime);

        Page<Process> workProcessPage = processMapper.selectPage(page, wrapper);
        if (workProcessPage == null){
            return Result.build(HttpCode.QUERY_FAILURE,"条件查询结果不存在");
        }
        return Result.ok(workProcessPage);
    }

    @Override
    public Result<List<Defect>> getDefectByProcessId(Long processId) {
        if (StrUtil.isBlank(String.valueOf(processId))) {
            return Result.build(HttpCode.QUERY_FAILURE, "传入的工序 id 不能为空");
        }
        // 校验工序 id 对应的工序是否存在。
        Process quiredProcess = processMapper.getProcessById(processId);
        if (quiredProcess == null) {
            return Result.build(HttpCode.QUERY_FAILURE, "传入的工序 id 对应的工序不存在");
        }

        // 返回工序不良品项详细信息。
        List<Defect> defectList = processAndDefectMapper.getDefectByProcessId(processId);
        return Result.ok(defectList);
    }

    @Override
    public Result<Object> saveProcess(Process process) {
        if(process == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工序对象不存在");
        }

        // 校验传输的工序名称。
        String processName = process.getProcessName();
        if (StrUtil.isBlank(processName)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工序名称不能为空");
        }
        String processCode = process.getProcessCode();
        if (StrUtil.isBlank(processCode)){
            // 未传输则生成默认编码。
            String generated = PrefixGenerateUtil.generateCode(processName);
            process.setProcessCode(generated);
        }else {
            // 已传输则校验数据库中是否已存在该编码对应的工序。
            Process quiredProcess = processMapper.getProcessByCode(processCode);
            if(quiredProcess != null){
                return Result.build(HttpCode.ADD_FAILURE,"已存在工序编码对应的工序，请更换工序编码或使用默认工序编码");
            }
        }
        // 校验传输的工序报工数配比。
        Double processRatio = process.getProcessRatio();
        if (processRatio == null || StrUtil.isBlank(String.valueOf(processRatio))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工序报工数配比不能为空");
        }
        // 校验传输的工序状态。
        Integer processStatus = process.getProcessStatus();
        if (processStatus == null || StrUtil.isBlank(String.valueOf(processStatus))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工序状态不能为空");
        }else if (ProcessStatus.isValidStatus(processStatus)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工序状态不合法");
        }

        // 校验全部通过则插入数据库中。
        processMapper.insert(process);
        return Result.build(HttpCode.SUCCESS, "新增工序成功");
    }

    @Override
    public Result<Object> updateProcess(Process process) {
        if(process == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工序对象不存在");
        }
        // 校验传输的工序对象 id 是否存在。
        Long processId = process.getId();
        if (StrUtil.isBlank(String.valueOf(processId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的修改的工序 id 不能为空");
        } else {
            // id 存在则校验数据库中是否存在对应的工序对象。
            Process quiredProcess = processMapper.getProcessById(processId);
            if (quiredProcess == null){
                return Result.build(HttpCode.MODIFICATION_FAILURE,"传入的工序 id 对应的工序不存在");
            }
        }

        // 校验通过则进行修改。
        processMapper.updateById(process);
        return Result.build(HttpCode.SUCCESS, "修改工序信息成功");
    }

    @Override
    public Result<Object> updateProcessStatus(Long processId, Integer status) {
        if(StrUtil.isBlank(String.valueOf(processId)) || status == null || StrUtil.isBlank(String.valueOf(status))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工序 id 或者修改的状态值不存在");
        }

        // 校验工序状态值是否合法。
        if (ProcessStatus.isValidStatus(status)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工序状态值不合法");
        }

        // 校验通过则进行修改。
        processMapper.updateProcessStatus(processId, status);
        return Result.build(HttpCode.SUCCESS, "修改工序状态成功");
    }

    @Override
    public Result<Object> deleteProcess(Long processId) {
        if(StrUtil.isBlank(String.valueOf(processId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的删除的工序 id 不能为空");
        }

        // 校验传输的工序对象 id 是否存在。
        Process quiredProcess = processMapper.getProcessById(processId);
        if (quiredProcess == null){
            return Result.build(HttpCode.MODIFICATION_FAILURE,"传入的工序 id 对应的工序不存在");
        }

        // 校验是否还有任务在使用该工序，若有则无法删除。
        List<Long> taskIdList = taskAndProcessMapper.getTaskIdByProcessId(processId);
        if (CollUtil.isNotEmpty(taskIdList)){
            return Result.build(HttpCode.MODIFICATION_FAILURE,"仍有任务在使用工序，无法删除");
        }

        // 校验通过则删除工序,并删除工序不良品项中间表记录。
        processMapper.deleteProcess(processId);
        processAndDefectMapper.deleteByProcessId(processId);
        return Result.build(HttpCode.SUCCESS, "删除工序成功");
    }

}
