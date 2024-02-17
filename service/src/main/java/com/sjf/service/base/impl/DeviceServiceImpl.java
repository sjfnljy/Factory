package com.sjf.service.base.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.component.ExcelListener;
import com.sjf.constant.DeviceStatus;
import com.sjf.constant.HttpCode;
import com.sjf.entity.base.Defect;
import com.sjf.entity.base.Device;
import com.sjf.entity.base.RepairRecord;
import com.sjf.exception.CustomException;
import com.sjf.mapper.base.DeviceAndRepairMapper;
import com.sjf.mapper.base.DeviceMapper;
import com.sjf.service.base.DeviceService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.base.DeviceQueryVo;
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
 * @Description: 设备管理 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/20 16:35
 */
@Slf4j
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DeviceAndRepairMapper deviceAndRepairMapper;

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里 URLEncoder.encode 可以防止中文乱码
            String fileName = URLEncoder.encode("设备数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 查询数据库中的数据。
            List<Device> deviceList = deviceMapper.selectList(null);
            // 写出数据到浏览器端。
            EasyExcel.write(response.getOutputStream(), Defect.class).sheet("设备数据").doWrite(deviceList);
        }catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public Result<Object> importData(MultipartFile file) {
        ExcelListener<Device> listener = new ExcelListener<>(deviceMapper);
        // 调用 read 方法读取 excel 数据。
        try {
            EasyExcel.read(file.getInputStream(),
                    Device.class,
                    listener).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(HttpCode.FILE_IMPORT_FAILURE,"导入 Excel 数据失败");
        }
        return Result.build(HttpCode.SUCCESS, "导入 Excel 数据成功");
    }


    @Override
    public Result<Page<Device>> conditionQuery(Integer current, Integer size, DeviceQueryVo deviceQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if (deviceQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))) {
            Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询条件为空");
        }
        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if (current <= 0 || size <= 0) {
            return Result.build(HttpCode.INVALID_PARAMETER, "传入的分页参数为不合法");
        }
        Page<Device> devicePage = new Page<>(current, size);

        // 获取传输的查询条件。
        String deviceCode = Objects.requireNonNull(deviceQueryVo).getDeviceCode();
        String deviceName = Objects.requireNonNull(deviceQueryVo).getDeviceName();
        Integer deviceStatus = Objects.requireNonNull(deviceQueryVo).getDeviceStatus();
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        // 设备名称模糊查询，设备编码、设备状态精确匹配：
        wrapper.like(StrUtil.isNotBlank(deviceName),Device::getDeviceName,deviceName)
                .or()
                .eq(StrUtil.isNotBlank(deviceCode),Device::getDeviceCode,deviceCode)
                .or()
                .eq(deviceStatus != null && StrUtil.isNotBlank(String.valueOf(deviceStatus)), Device::getDeviceStatus, deviceStatus);
        // 优先显示未启动和运行中的设备。
        wrapper.orderByAsc(Device::getDeviceStatus);
        Page<Device> page = deviceMapper.selectPage(devicePage, wrapper);
        if(page == null){
            return Result.build(HttpCode.SUCCESS,"条件查询结果不存在");
        }

        // 查询每个设备对应的维修记录信息。
        List<Device> deviceList = page.getRecords();
        for (Device device : deviceList) {
            if (device != null) {
                List<Long> repairRecordIdList = deviceAndRepairMapper.getRepairRecordIdsByDeviceId(device.getId());
                device.setRepairRecordIdList(repairRecordIdList);
            }
        }
        page.setRecords(deviceList);
        // 查询成功返回分页结果信息。
        return Result.ok(page);
    }

    @Override
    public Result<List<RepairRecord>> getDeviceRepairRecordsById(Long deviceId) {
        if (StrUtil.isBlank(String.valueOf(deviceId))) {
            return Result.build(HttpCode.QUERY_FAILURE, "传入的设备 id 不能为空");
        }

        // 校验传入的设备 id 对应的对象是否存在。
        Device device = deviceMapper.getDeviceByDeviceId(deviceId);
        if (device == null){
            return Result.build(HttpCode.QUERY_FAILURE,"不存在该设备 id 对应的设备对象");
        }
        // 设备存在则获取维修记录信息并返回。
        List<RepairRecord> repairRecordList = deviceAndRepairMapper.getRepairRecordByDeviceId(device.getId());
        return Result.ok(repairRecordList);
    }

    @Override
    public Result<Object> saveDevice(Device device) {
        if (device == null) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的设备对象不存在");
        }

        // 校验传输的设备名称。
        String deviceName = device.getDeviceName();
        if (StrUtil.isBlank(deviceName)){
            return Result.build(HttpCode.ADD_FAILURE,"设备名称不允许为空");
        }else {
            Device quiredDevice = deviceMapper.getDeviceByDeviceName(deviceName);
            if (quiredDevice != null){
                return Result.build(HttpCode.ADD_FAILURE,"设备名称已存在");
            }
        }
        // 校验传输的设备编码。
        String deviceCode = device.getDeviceCode();
        if (StrUtil.isBlank(deviceCode)){
            String generateCode = PrefixGenerateUtil.generateCode(deviceName);
            device.setDeviceCode(generateCode);
        }else {
            Device quiredDevice = deviceMapper.getDeviceByDeviceCode(deviceCode);
            if (quiredDevice!= null){
                return Result.build(HttpCode.ADD_FAILURE,"设备编码已存在");
            }
        }
        // 校验传输的设备状态。
        Integer deviceStatus = device.getDeviceStatus();
        if (deviceStatus == null){
            return Result.build(HttpCode.ADD_FAILURE,"设备状态不允许为空");
        }else if (DeviceStatus.isValidStatus(deviceStatus)){
            return Result.build(HttpCode.ADD_FAILURE,"传输的设备状态值不合法");
        }
        // 校验传输的设备厂商。
        String manufacturer = device.getManufacturer();
        if (StrUtil.isBlank(manufacturer)){
            return Result.build(HttpCode.ADD_FAILURE,"设备厂商不允许为空");
        }

        // 校验成功则插入数据库中。
        deviceMapper.insert(device);
        return Result.build(HttpCode.SUCCESS, "新增设备成功");
    }

    @Override
    public Result<Object> updateDevice(Device device) {
        if (device == null) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的设备对象不存在");
        }
        // 校验传输的设备 id 是否存在：
        Long deviceId = device.getId();
        if (StrUtil.isBlank(String.valueOf(deviceId))) {
            Result.build(HttpCode.MODIFICATION_FAILURE, "修改设备信息时必须传输修改的设备 id");
        } else {
            // 校验设备 id 对应的设备对象是否存在。
            Device quiredDevice = deviceMapper.getDeviceByDeviceId(deviceId);
            if (quiredDevice == null) {
                return Result.build(HttpCode.MODIFICATION_FAILURE, "修改的设备对象不存在");
            }
        }

        // 如果要修改设备状态则校验设备状态是否合理。
        Integer deviceStatus = device.getDeviceStatus();
        if (deviceStatus != null && DeviceStatus.isValidStatus(deviceStatus)){
            return Result.build(HttpCode.MODIFICATION_FAILURE, "修改的设备状态值不合法");
        }

        // 校验通过则进行修改。
        deviceMapper.updateById(device);
        return Result.build(HttpCode.SUCCESS, "修改设备信息成功");
    }

    @Override
    public Result<Object> deleteDevice(Long deviceId) {
        if (StrUtil.isBlank(String.valueOf(deviceId))) {
            return Result.build(HttpCode.UNPASSED_PARAMETER, "传入的删除的设备 id 不能为空");
        }else {
            // 校验设备 id 对应的设备对象是否存在。
            Device quiredDevice = deviceMapper.getDeviceByDeviceId(deviceId);
            if (quiredDevice == null) {
                return Result.build(HttpCode.MODIFICATION_FAILURE, "修改的设备对象不存在");
            }
        }

        // 校验通过则删除设备信息以及对应的维修记录。
        deviceMapper.deleteDevice(deviceId);
        deviceAndRepairMapper.deleteDeviceAndRepairByDeviceId(deviceId);
        return Result.build(HttpCode.SUCCESS,"删除设备信息成功");
    }


}
