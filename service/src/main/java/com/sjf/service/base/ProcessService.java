package com.sjf.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.base.Defect;
import com.sjf.entity.base.Process;
import com.sjf.vo.base.ProcessQueryVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 工序管理 Service 接口
 * @Author: SJF
 * @Date: 2024/1/23 17:29
 */

public interface ProcessService extends IService<Process> {

    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 当前页
     * @param size: 每页显示的条数
     * @param processQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<Process>> conditionQuery(Integer current, Integer size, ProcessQueryVo processQueryVo);

    /**
     * 导出工序数据到 Excel 中
     * @param response: 响应
     */
    void exportData(HttpServletResponse response);

    /**
     * 导入 Excel 工序数据
     * @param file: Excel 文件
     * @return: 导入数据的结果信息
     */
    Result<Object> importData(MultipartFile file);

    /**
     * 根据工序 id 获取工序不良品项信息
     * @param processId: 工序 id
     * @return: 不良品项集合
     */
    Result<List<Defect>> getDefectByProcessId(Long processId);

    /**
     * 校验新增的工序对象并完成新增
     * @param process: 传入的新增的工序对象
     * @return: 校验及新增的结果信息
     */
    Result<Object> saveProcess(Process process);

    /**
     * 校验修改的工序对象并完成修改
     * @param process: 传入的修改的工序对象
     * @return: 校验及修改的结果信息
     */
    Result<Object> updateProcess(Process process);

    /**
     * 修改指定工序 id 的工序状态。
     * @param processId:工序 id
     * @param status:工序状态
     * @return: 修改的结果信息
     */
    Result<Object> updateProcessStatus(Long processId, Integer status);

    /**
     * 根据传输的工序 id 删除工序
     * @param processId: 传入的删除的工序 id
     * @return: 校验及删除的结果信息
     */
    Result<Object> deleteProcess(Long processId);
}
