package com.sjf.service.base.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.component.ExcelListener;
import com.sjf.constant.HttpCode;
import com.sjf.entity.base.Process;
import com.sjf.entity.base.ProcessRouter;
import com.sjf.entity.base.RouterAndProcess;
import com.sjf.exception.CustomException;
import com.sjf.mapper.base.ProcessRouterMapper;
import com.sjf.mapper.base.RouterAndProcessMapper;
import com.sjf.service.base.ProcessRouterService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.base.RouterAssignProcessVo;
import com.sjf.vo.base.RouterQueryVo;
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
 * @Description: 工艺路线管理 Service 接口实现类
 * @Author: SJF
 * @Date: 2024/1/24 22:05
 */

@Service
@Slf4j
public class ProcessRouterServiceImpl extends ServiceImpl<ProcessRouterMapper, ProcessRouter> implements ProcessRouterService {

    @Resource
    private ProcessRouterMapper processRouterMapper;

    @Resource
    private RouterAndProcessMapper routerAndProcessMapper;


    @Override
    public Result<Page<ProcessRouter>> conditionQuery(Integer current, Integer size, RouterQueryVo routerQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if (routerQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))) {
            Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询条件为空");
        }
        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if (current <= 0 || size <= 0) {
            return Result.build(HttpCode.INVALID_PARAMETER, "传入的分页参数为不合法");
        }
        Page<ProcessRouter> page = new Page<>(current, size);

        // 获取传输的查询条件。
        String routerCode = Objects.requireNonNull(routerQueryVo).getRouterCode();
        String routerName = Objects.requireNonNull(routerQueryVo).getRouterName();
        LambdaQueryWrapper<ProcessRouter> wrapper = new LambdaQueryWrapper<>();
        // 工艺路线编码精确查询、工艺路线名称模糊查询。
        wrapper.eq(StrUtil.isNotBlank(routerCode),ProcessRouter::getRouterCode,routerCode)
                .like(StrUtil.isNotBlank(routerName),ProcessRouter::getRouterName,routerName);
        wrapper.orderByDesc(ProcessRouter::getUpdateTime);
        Page<ProcessRouter> processRouterPage = processRouterMapper.selectPage(page, wrapper);
        if(processRouterPage == null){
            return Result.build(HttpCode.QUERY_FAILURE,"条件查询结果不存在");
        }

        // 查询成功则返回结果。
        return Result.ok(processRouterPage);
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里 URLEncoder.encode 可以防止中文乱码
            String fileName = URLEncoder.encode("工艺路线数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 查询数据库中的数据。
            List<ProcessRouter> repairRecordList = processRouterMapper.selectList(null);
            // 写出数据到浏览器端。
            EasyExcel.write(response.getOutputStream(), ProcessRouter.class).sheet("工艺路线数据").doWrite(repairRecordList);
        }catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public Result<Object> importData(MultipartFile file) {
        ExcelListener<ProcessRouter> listener = new ExcelListener<>(processRouterMapper);
        // 调用 read 方法读取 excel 数据。
        try {
            EasyExcel.read(file.getInputStream(),
                    ProcessRouter.class,
                    listener).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(HttpCode.FILE_IMPORT_FAILURE,"导入 Excel 数据失败");
        }
        return Result.build(HttpCode.SUCCESS, "导入 Excel 数据成功");
    }

    @Override
    public Result<List<Process>> getProcessByRouterId(Long routerId) {
        // 校验传入的工艺路线 id。
        if (StrUtil.isBlank(String.valueOf(routerId))) {
            return Result.build(HttpCode.QUERY_FAILURE, "传入的工艺路线 id 不能为空");
        } else {
            ProcessRouter quiredProcessRouter =  processRouterMapper.getRouterById(routerId);
            if (quiredProcessRouter == null) {
                return Result.build(HttpCode.QUERY_FAILURE, "传入的工艺路线 id 对应的工艺路线不存在");
            }
        }

        // 校验通过则进行查询。
        List<Process> processList = routerAndProcessMapper.getProcessesByRouterId(routerId);
        return Result.ok(processList);
    }

    @Override
    public Result<Object> saveProcessRouter(ProcessRouter processRouter) {
        if(processRouter == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工艺路线对象不存在");
        }

        // 校验传输的工艺路线名称。
        String routerName = Objects.requireNonNull(processRouter).getRouterName();
        if (StrUtil.isBlank(routerName)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工艺路线名称不能为空");
        }
        // 校验传输的工艺路线编码:
        String routerCode = Objects.requireNonNull(processRouter).getRouterCode();
        if (StrUtil.isBlank(routerCode)){
            // 未传输则生成默认编码。
            String generated = PrefixGenerateUtil.generateCode(routerName);
            processRouter.setRouterCode(generated);
        }else {
            // 已传输则校验数据库中是否已存在该编码对应的工艺路线。
            ProcessRouter quiredProcessRouter = processRouterMapper.getRouterByRouterCode(routerCode);
            if(quiredProcessRouter != null){
                return Result.build(HttpCode.ADD_FAILURE,"已存在工艺路线编码对应的工艺路线，请更换工艺路线编码或使用默认工艺路线编码");
            }
        }

        // 校验全部通过则插入数据库中。
        int insert = processRouterMapper.insert(processRouter);
        return Result.build(HttpCode.SUCCESS, "插入工艺路线数据成功");
    }

    @Override
    public Result<Object> updateProcessRouter(ProcessRouter processRouter) {
        if(processRouter == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工艺路线对象不存在");
        }
        // 校验传输的工序对象 id 是否存在。
        Long routerId = Objects.requireNonNull(processRouter).getId();
        if (routerId == null || StrUtil.isBlank(String.valueOf(routerId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的工艺路线 id 不能为空");
        }else {
            ProcessRouter quiredProcessRouter =  processRouterMapper.getRouterById(routerId);
            if (quiredProcessRouter == null) {
                return Result.build(HttpCode.QUERY_FAILURE, "传入的工艺路线 id 对应的工艺路线不存在");
            }
        }

        // 校验通过则进行修改。
        int update = processRouterMapper.updateById(processRouter);
        return Result.build(HttpCode.SUCCESS, "修改工艺路线数据成功");
    }

    @Override
    public Result<Object> deleteProcessRouter(Long routerId) {
        if (StrUtil.isBlank(String.valueOf(routerId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的删除的工艺路线 id 不能为空");
        } else {
            ProcessRouter quiredProcessRouter =  processRouterMapper.getRouterById(routerId);
            if (quiredProcessRouter == null) {
                return Result.build(HttpCode.QUERY_FAILURE, "传入的工艺路线 id 对应的工艺路线不存在");
            }
        }

        // 校验通过则删除工艺路线,并删除工艺路线-工序中间表记录。
        processRouterMapper.deleteRouterById(routerId);
        routerAndProcessMapper.deleteRouterProcessByRouterId(routerId);
        return Result.build(HttpCode.SUCCESS, "删除工艺路线数据成功");
    }

    @Override
    public Result<Object> assignProcess(RouterAssignProcessVo routerAssignProcessVo) {
        if (routerAssignProcessVo == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的工艺路线分配工序对象不存在");
        }

        // 先删除中间表记录。
        Long routerId = routerAssignProcessVo.getRouterId();
        routerAndProcessMapper.deleteRouterProcessByRouterId(routerId);

        // 再插入中间表记录。
        for (Long processId : routerAssignProcessVo.getProcessIdList()){
            if (processId != null){
                RouterAndProcess routerAndProcess = new RouterAndProcess(routerId, processId);
                routerAndProcessMapper.insert(routerAndProcess);
            }
        }
        return Result.build(HttpCode.SUCCESS, "分配工艺路线工序成功");
    }

}
