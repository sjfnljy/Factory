package com.sjf.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.base.RepairRecord;
import com.sjf.vo.base.RepairRecordQueryVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 设备维修记录 Service 接口
 * @Author: SJF
 * @Date: 2024/1/22 19:39
 */

public interface RepairRecordService extends IService<RepairRecord> {
    /**
     * 导出维修记录数据到 Excel 中
     * @param response: 响应
     */
    void exportData(HttpServletResponse response);

    /**
     * 导入 Excel 维修记录数据
     * @param file: Excel 文件
     * @return: 导入数据的结果信息
     */
    Result<Object> importData(MultipartFile file);

    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 当前页
     * @param size: 每页显示的条数
     * @param recordQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<RepairRecord>> conditionQuery(Integer current, Integer size, RepairRecordQueryVo recordQueryVo);

    /**
     * 根据传输的设备 id 新增设备维修记录
     * @param deviceId: 设备 id
     * @param repairRecord: 维修记录对象
     * @return: 新增的结果信息
     */
    Result<Object> saveRepairRecord(Long deviceId, RepairRecord repairRecord);

    /**
     * 根据传输的维修记录 id 删除设备维修记录
     * @param repairId: 维修记录 id
     * @return: 删除的结果信息
     */
    Result<Object> deleteRepairRecord(Long repairId);
}
