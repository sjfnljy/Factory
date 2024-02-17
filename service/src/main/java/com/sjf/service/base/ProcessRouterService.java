package com.sjf.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.base.Process;
import com.sjf.entity.base.ProcessRouter;
import com.sjf.vo.base.RouterAssignProcessVo;
import com.sjf.vo.base.RouterQueryVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 工艺路线管理 Service 接口
 * @Author: SJF
 * @Date: 2024/1/24 22:05
 */

public interface ProcessRouterService extends IService<ProcessRouter> {

    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 当前页
     * @param size: 每页显示的条数
     * @param routerQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<ProcessRouter>> conditionQuery(Integer current, Integer size, RouterQueryVo routerQueryVo);

    /**
     * 导出工艺路线数据到 Excel 中
     * @param response: 响应
     */
    void exportData(HttpServletResponse response);

    /**
     * 导入 Excel 工艺路线数据
     * @param file: Excel 文件
     * @return: 导入数据的结果信息
     */
    Result<Object> importData(MultipartFile file);

    /**
     * 根据工艺路线 id 获取工艺路线对应的工序信息
     * @param routerId: 工艺路线 id
     * @return: 工序集合
     */
    Result<List<Process>> getProcessByRouterId(Long routerId);

    /**
     * 校验新增的工艺路线对象并完成新增
     * @param processRouter: 传入的新增的工艺路线对象
     * @return: 校验及新增的结果信息
     */
    Result<Object> saveProcessRouter(ProcessRouter processRouter);

    /**
     * 校验修改的工艺路线对象并完成修改
     * @param processRouter: 传入的修改的工艺路线对象
     * @return: 校验及修改的结果信息
     */
    Result<Object> updateProcessRouter(ProcessRouter processRouter);

    /**
     * 根据传输的工艺路线 id 删除工艺路线
     * @param routerId: 传入的删除的工艺路线 id
     * @return: 校验及删除的结果信息
     */
    Result<Object> deleteProcessRouter(Long routerId);

    /**
     * 为工艺路线分配工序
     * @param routerAssignProcessVo: 工艺路线分配工序对象
     * @return: 分配的结果信息
     */
    Result<Object> assignProcess(RouterAssignProcessVo routerAssignProcessVo);
}
