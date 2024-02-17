package com.sjf.service.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjf.common.Result;
import com.sjf.entity.base.Product;
import com.sjf.vo.base.ProductQueryVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 产品管理 Service 接口
 * @Author: SJF
 * @Date: 2024/1/27 15:35
 */

public interface ProductService extends IService<Product> {
    /**
     * 根据传输的查询条件进行条件分页查询
     * @param current: 当前页
     * @param size: 每页显示的条数
     * @param productQueryVo: 传输的查询条件对象
     * @return: 查询的结果信息
     */
    Result<Page<Product>> conditionQuery(Integer current, Integer size, ProductQueryVo productQueryVo);

    /**
     * 导出产品数据到 Excel 中
     * @param response: 响应
     */
    void exportData(HttpServletResponse response);

    /**
     * 导入 Excel 产品数据
     * @param file: Excel 文件
     * @return: 导入数据的结果信息
     */
    Result<Object> importData(MultipartFile file);

    /**
     * 校验新增的产品对象并完成新增
     * @param product: 传入的新增的产品对象
     * @return: 校验及新增的结果信息
     */
    Result<Object> saveProduct(Product product);

    /**
     * 校验修改的产品对象并完成修改
     * @param product: 传入的修改的产品对象
     * @return: 校验及修改的结果信息
     */
    Result<Object> updateProduct(Product product);

    /**
     * 根据传输的产品 id 删除对象
     * @param productId: 传入的删除的产品 id
     * @return: 校验及删除的结果信息
     */
    Result<Object> deleteProduct(Long productId);
}
