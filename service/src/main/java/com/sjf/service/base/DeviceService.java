package com.sjf.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.base.Device;
import com.sjf.entity.base.RepairRecord;
import com.sjf.vo.base.DeviceQueryVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 设备管理 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/20 16:33
 */

public interface DeviceService extends IService<Device> {

    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 当前页
     * @param size: 每页显示的条数
     * @param deviceQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<Device>> conditionQuery(Integer current, Integer size, DeviceQueryVo deviceQueryVo);

    /**
     * 导出设备数据到 Excel 中
     * @param response: 响应
     */
    void exportData(HttpServletResponse response);

    /**
     * 导入 Excel 设备数据
     * @param file: Excel 文件
     * @return: 导入数据的结果信息
     */
    Result<Object> importData(MultipartFile file);

    /**
     * 根据传输的设备 id 查询设备的维修记录详细信息
     * @param deviceId: 传入的查询的设备 id
     * @return: 查询的结果信息
     */
    Result<List<RepairRecord>> getDeviceRepairRecordsById(Long deviceId);

    /**
     * 校验新增的设备对象并完成新增
     * @param device: 传入的新增的设备对象
     * @return: 校验及新增的结果信息
     */
    Result<Object> saveDevice(Device device);

    /**
     * 校验修改的设备对象并完成修改
     * @param device: 传入的修改的设备对象
     * @return: 校验及修改的结果信息
     */
    Result<Object> updateDevice(Device device);

    /**
     * 根据传输的设备 id 删除对象
     * @param deviceId: 传入的删除的设备 id
     * @return: 校验及删除的结果信息
     */
    Result<Object> deleteDevice(Long deviceId);
}
