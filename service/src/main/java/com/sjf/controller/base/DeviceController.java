package com.sjf.controller.base;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.base.Device;
import com.sjf.entity.base.RepairRecord;
import com.sjf.service.base.DeviceService;
import com.sjf.vo.base.DeviceQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 设备请求处理器
 * @Author: SJF
 * @Date: 2024/1/20 16:32
 */

@Api(tags = "基础数据设备管理接口")
@RestController
@RequestMapping("/factory/base/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @GetMapping(value = "/exportData")
    @ApiOperation("导出设备数据")
    public void exportData(HttpServletResponse response) {
       deviceService.exportData(response);
    }

    @PostMapping("/importData")
    @ApiOperation("导入设备数据")
    public Result<Object> importData(MultipartFile file) {
        return  deviceService.importData(file);
    }

    @ApiOperation("查询所有设备")
    @GetMapping("/all")
    public Result<List<Device>> getAllDevice() {
        List<Device> deviceList = deviceService.list();
        if (CollUtil.isEmpty(deviceList)) {
            return Result.build(HttpCode.QUERY_FAILURE, "查询结果不存在");
        }
        return Result.ok(deviceList);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<Device>> conditionQuery(@PathVariable("current") Integer current,
                                               @PathVariable("size") Integer size,
                                               DeviceQueryVo deviceQueryVo) {
        return deviceService.conditionQuery(current, size, deviceQueryVo);
    }

    @ApiOperation("根据设备主键 id 查询设备维修记录详细信息")
    @GetMapping("/get/repairRecord/{deviceId}")
    public Result<List<RepairRecord>> getDeviceById(@PathVariable("deviceId") Long deviceId) {
        return deviceService.getDeviceRepairRecordsById(deviceId);
    }

    @ApiOperation("新增设备")
    @PostMapping("/save")
    public Result<Object> saveDevice(@RequestBody Device device) {
        return deviceService.saveDevice(device);
    }

    @ApiOperation("修改设备信息")
    @PutMapping("/update")
    public Result<Object> updateDevice(@RequestBody Device device) {
        return deviceService.updateDevice(device);
    }

    @ApiOperation("根据设备 id 删除设备")
    @DeleteMapping("/delete/{deviceId}")
    public Result<Object> deleteDevice(@PathVariable("deviceId") Long deviceId) {
        return deviceService.deleteDevice(deviceId);
    }

}
