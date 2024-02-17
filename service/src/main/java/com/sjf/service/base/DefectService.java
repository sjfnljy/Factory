package com.sjf.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.base.Defect;
import com.sjf.vo.base.DefectQueryVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 不良品项类型 Service 接口
 * @Author: SJF
 * @Date: 2024/1/19 15:37
 */

public interface DefectService extends IService<Defect> {
    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 当前页
     * @param size: 每页显示的条数
     * @param defectQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<Defect>> conditionQuery(Integer current, Integer size, DefectQueryVo defectQueryVo);

    /**
     * 导出不良品项数据到 Excel 中
     * @param response: 响应
     */
    void exportData(HttpServletResponse response);

    /**
     * 导入 Excel 不良品项数据
     * @param file: Excel 文件
     * @return: 导入数据的结果信息
     */
    Result<Object> importData(MultipartFile file);

    /**
     * 根据不良品项类型 id 查询不良品项类型详细信息
     * @param defectId: 不良品项类型 id
     * @return: 不良品项类型详细信息
     */
    Result<Defect> getDefectById(Long defectId);

    /**
     * 校验新增的不良品项对象并完成新增
     * @param defect: 传入的新增的不良品项对象
     * @return: 校验及新增的结果信息
     */
    Result<Object> SaveDefect(Defect defect);

    /**
     * 校验修改的不良品项对象并完成修改
     * @param defect: 传入的修改的不良品项对象
     * @return: 校验及修改的结果信息
     */
    Result<Object> UpdateDefect(Defect defect);

    /**
     * 根据传输的不良品项 id 删除对象
     * @param defectId: 传入的删除的不良品项 id
     * @return: 校验及删除的结果信息
     */
    Result<Object> DeleteDefect(Long defectId);
}
