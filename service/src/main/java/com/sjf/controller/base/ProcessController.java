package com.sjf.controller.base;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.base.Defect;
import com.sjf.entity.base.Process;
import com.sjf.service.base.ProcessService;
import com.sjf.vo.base.ProcessQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 工序管理请求处理器
 * @Author: SJF
 * @Date: 2024/1/23 17:31
 */

@Api(tags = "工序管理接口")
@RestController
@RequestMapping("/factory/base/process")
public class ProcessController {

    @Resource
    private ProcessService processService;

    @GetMapping(value = "/exportData")
    @ApiOperation("导出工序数据")
    public void exportData(HttpServletResponse response) {
        processService.exportData(response);
    }

    @PostMapping("/importData")
    @ApiOperation("导入工序数据")
    public Result<Object> importData(MultipartFile file) {
        return  processService.importData(file);
    }

    @ApiOperation("查询所有工序")
    @GetMapping("/all")
    public Result<List<Process>> getAllProcess(){
        List<Process> processList = processService.list();
        if(CollUtil.isEmpty(processList)){
            return Result.build(HttpCode.SUCCESS,"查询结果不存在");
        }
        return Result.ok(processList);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<Process>> conditionQuery(@PathVariable("current") Integer current,
                                                @PathVariable("size") Integer size,
                                                ProcessQueryVo processQueryVo) {
        return processService.conditionQuery(current, size, processQueryVo);
    }

    @ApiOperation("根据工序主键 id 查询工序不良品项详细信息")
    @GetMapping("/get/{processId}")
    public Result<List<Defect>> getProcessById(@PathVariable("processId") Long processId) {
        return processService.getDefectByProcessId(processId);
    }

    @ApiOperation("新增工序")
    @PostMapping("/save")
    public Result<Object> saveProcess(@RequestBody Process process){

        return processService.saveProcess(process);
    }

    @ApiOperation("修改工序信息")
    @PutMapping("/update")
    public Result<Object> updateProcess(@RequestBody Process process){
        return processService.updateProcess(process);
    }

    @ApiOperation("修改工序状态")
    @PutMapping("/update/status/{processId}/{status}")
    public Result<Object> updateProcessStatus(@PathVariable("processId") Long processId,
                                              @PathVariable("status") Integer status){
        return processService.updateProcessStatus(processId,status);
    }

    @ApiOperation("根据工序 id 删除工序")
    @DeleteMapping("/delete/{processId}")
    public Result<Object> deleteProcess(@PathVariable("processId") Long processId){
        return processService.deleteProcess(processId);
    }
}
