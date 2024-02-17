package com.sjf.service.base.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.component.ExcelListener;
import com.sjf.constant.HttpCode;
import com.sjf.entity.base.Device;
import com.sjf.entity.base.DeviceAndRepairRecord;
import com.sjf.entity.base.RepairRecord;
import com.sjf.exception.CustomException;
import com.sjf.mapper.base.DeviceAndRepairMapper;
import com.sjf.mapper.base.DeviceMapper;
import com.sjf.mapper.base.RepairRecordMapper;
import com.sjf.service.base.RepairRecordService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.base.RepairRecordQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 设备维修记录 Service 接口实现类
 * @Author: SJF
 * @Date: 2024/1/22 19:40
 */

@Service
@Slf4j
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements RepairRecordService {

    @Resource
    private RepairRecordMapper repairRecordMapper;

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
            String fileName = URLEncoder.encode("维修记录数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 查询数据库中的数据。
            List<RepairRecord> repairRecordList = repairRecordMapper.selectList(null);
            // 写出数据到浏览器端。
            EasyExcel.write(response.getOutputStream(), RepairRecord.class).sheet("维修记录数据").doWrite(repairRecordList);
        }catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public Result<Object> importData(MultipartFile file) {
        ExcelListener<RepairRecord> listener = new ExcelListener<>(repairRecordMapper);
        // 调用 read 方法读取 excel 数据。
        try {
            EasyExcel.read(file.getInputStream(),
                    RepairRecord.class,
                    listener).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(HttpCode.FILE_IMPORT_FAILURE,"导入 Excel 数据失败");
        }
        return Result.build(HttpCode.SUCCESS, "导入 Excel 数据成功");
    }

    @Override
    public Result<Page<RepairRecord>> conditionQuery(Integer current, Integer size, RepairRecordQueryVo recordQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if (recordQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))) {
            Result.build(HttpCode.UNPASSED_PARAMETER, "传入的查询条件为空");
        }
        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if (current <= 0 || size <= 0) {
            return Result.build(HttpCode.INVALID_PARAMETER, "传入的分页参数为不合法");
        }
        Page<RepairRecord> recordPage = new Page<>(current, size);

        // 获取传输的查询条件。
        String repairCode = Objects.requireNonNull(recordQueryVo).getRepairCode();
        BigDecimal repairCost =  Objects.requireNonNull(recordQueryVo).getRepairCost();
        String repairPersonName =  Objects.requireNonNull(recordQueryVo).getRepairPersonName();
        LambdaQueryWrapper<RepairRecord> wrapper = new LambdaQueryWrapper<>();
        // 维修记录编号精确匹配，维修记录金额范围查询，维修人员名称模糊查询。
        wrapper.eq(StrUtil.isNotBlank(repairCode),RepairRecord::getRepairCode,repairCode)
                .ge(repairCost != null && StrUtil.isNotBlank(String.valueOf(repairCost)),RepairRecord::getRepairCost,repairCost)
                .like(StrUtil.isNotBlank(repairPersonName),RepairRecord::getRepairPersonName,repairPersonName);
        wrapper.orderByDesc(RepairRecord::getUpdateTime);

        // 执行查询并返回分页结果信息。
        Page<RepairRecord> page = repairRecordMapper.selectPage(recordPage, wrapper);
        if (page == null){
            return Result.build(HttpCode.SUCCESS,"条件查询结果不存在");
        }
        return Result.ok(page);
    }

    @Override
    public Result<Object> saveRepairRecord(Long deviceId, RepairRecord repairRecord) {
        // 校验传入的设备 id。
        if (StrUtil.isBlank(String.valueOf(deviceId))) {
            return Result.build(HttpCode.ADD_FAILURE, "传入的设备 id 不能为空");
        }else {
            Device quiredDevice = deviceMapper.getDeviceByDeviceId(deviceId);
            if (quiredDevice == null){
                return Result.build(HttpCode.ADD_FAILURE, "不存在传入的设备 id 对应的设备");
            }
        }

        // 校验传入的维修记录对象。
        if(repairRecord == null){
            return Result.build(HttpCode.ADD_FAILURE, "传入的维修记录对象不能为空");
        }else {
            // 校验传输的维修记录编号：没传输则生成，已传输则校验编号是否存在。
            String repairCode = repairRecord.getRepairCode();
            if(StrUtil.isBlank(repairCode) ){
                String generateCode = PrefixGenerateUtil.generateCode(String.valueOf(deviceId));
                repairRecord.setRepairCode(generateCode);
            }else {
                RepairRecord quiredRepairRecord = repairRecordMapper.getRepairRecordByCode(repairCode);
                if (quiredRepairRecord != null){
                    return Result.build(HttpCode.ADD_FAILURE, "传入的维修记录编号已存在");
                }
            }

            // 校验此次设备故障原因：
            String faultCause = repairRecord.getFaultCause();
            if (StrUtil.isBlank(String.valueOf(faultCause))){
                return Result.build(HttpCode.ADD_FAILURE,"必须传输此次维修的故障原因");
            }

            // 校验传输的维修人姓名和维修人电话号码。
            String repairPersonName = repairRecord.getRepairPersonName();
            String repairPersonPhone = repairRecord.getRepairPersonPhone();
            if(StrUtil.isBlank(repairPersonName) || StrUtil.isBlank(repairPersonPhone)){
                return Result.build(HttpCode.ADD_FAILURE, "传入的维修人姓名和电话号码不能为空");
            }else if (!Validator.isMobile(repairPersonPhone)){
                return Result.build(HttpCode.ADD_FAILURE, "传入的维修人电话号码不符合规定");
            }

            // 校验传输的维修记录金额。
            BigDecimal repairCost =  Objects.requireNonNull(repairRecord).getRepairCost();
            if(repairCost == null){
                return Result.build(HttpCode.ADD_FAILURE, "传入的维修记录金额不能为空");
            }else if (repairCost.compareTo(new BigDecimal(0)) <= 0){
                return Result.build(HttpCode.ADD_FAILURE, "传入的维修记录金额不能小于等于 0");
            }

            // 校验传输的维修记录结果。
            String repairResult =  Objects.requireNonNull(repairRecord).getRepairResult();
            if(StrUtil.isBlank(repairResult)){
                return Result.build(HttpCode.ADD_FAILURE, "传入的维修记录结果不能为空");
            }
        }

        // 校验通过则插入维修记录与设备维修记录中间表记录。
        repairRecordMapper.insert(repairRecord);
        deviceAndRepairMapper.insert(new DeviceAndRepairRecord(deviceId,repairRecord.getId()));
        return Result.build(HttpCode.SUCCESS, "添加设备维修记录成功");
    }

    @Override
    public Result<Object> deleteRepairRecord(Long repairId) {
        if (StrUtil.isBlank(String.valueOf(repairId))){
            return Result.build(HttpCode.DELETION_FAILURE, "传入的维修记录 id 不能为空");
        }
        // 校验传入的维修记录 id 是否存在。
        RepairRecord quiredRepairRecord =  repairRecordMapper.getRepairRecordById(repairId);
        if (quiredRepairRecord == null){
            return Result.build(HttpCode.DELETION_FAILURE, "不存在传入的维修记录 id 对应的维修记录");
        }
        // 校验通过则删除维修记录。
        repairRecordMapper.deleteById(repairId);
        return Result.build(HttpCode.SUCCESS, "删除设备维修记录成功");
    }
}
