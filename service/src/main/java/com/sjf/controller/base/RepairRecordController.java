package com.sjf.controller.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.entity.base.RepairRecord;
import com.sjf.service.base.RepairRecordService;
import com.sjf.vo.base.RepairRecordQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 设备维修记录请求处理器
 * @Author: SJF
 * @Date: 2024/1/22 19:38
 */

@Api(tags = "基础数据设备维修记录管理接口")
@RestController
@RequestMapping("/factory/base/repairRecord")
public class RepairRecordController {

    @Resource
    private RepairRecordService repairRecordService;

    @GetMapping(value = "/exportData")
    @ApiOperation("导出维修记录数据")
    public void exportData(HttpServletResponse response) {
        repairRecordService.exportData(response);
    }

    @PostMapping("/importData")
    @ApiOperation("导入维修记录数据")
    public Result<Object> importData(MultipartFile file) {
        return  repairRecordService.importData(file);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<RepairRecord>> conditionQuery(@PathVariable("current") Integer current,
                                                     @PathVariable("size") Integer size,
                                                     RepairRecordQueryVo recordQueryVo) {
        return repairRecordService.conditionQuery(current, size, recordQueryVo);
    }

    @ApiOperation("根据设备 id 新增设备维修记录")
    @PostMapping("/save/{deviceId}")
    public Result<Object> saveDeviceRepairRecord(@PathVariable("deviceId") Long deviceId,
                                                 @RequestBody RepairRecord repairRecord){
        return repairRecordService.saveRepairRecord(deviceId,repairRecord);
    }

    @ApiOperation("删除设备维修记录")
    @DeleteMapping("/delete/{repairId}")
    public Result<Object> deleteDeviceRepairRecord(@PathVariable("repairId") Long repairId){
        return repairRecordService.deleteRepairRecord(repairId);
    }
}
