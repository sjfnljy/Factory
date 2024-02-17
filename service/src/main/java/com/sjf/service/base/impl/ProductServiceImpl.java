package com.sjf.service.base.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjf.common.Result;
import com.sjf.component.ExcelListener;
import com.sjf.constant.HttpCode;
import com.sjf.constant.ProduceType;
import com.sjf.entity.base.Product;
import com.sjf.exception.CustomException;
import com.sjf.mapper.base.ProductMapper;
import com.sjf.mapper.produce.WorkOrderAndProductMapper;
import com.sjf.service.base.ProductService;
import com.sjf.utils.PrefixGenerateUtil;
import com.sjf.vo.base.ProductQueryVo;
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
 * @Description: 产品管理 Service 接口实现类
 * @Author: SJF
 * @Date: 2024/1/27 15:35
 */

@Service
@Slf4j
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private WorkOrderAndProductMapper workOrderAndProductMapper;


    @Override
    public Result<Page<Product>> conditionQuery(Integer current, Integer size, ProductQueryVo productQueryVo) {
        // 判断是否传入分页查询参数以及条件查询对象。
        if(productQueryVo == null || StrUtil.isBlank(String.valueOf(current))
                || StrUtil.isBlank(String.valueOf(size))){
            Result.build(HttpCode.UNPASSED_PARAMETER,"传入的查询条件为空");
        }
        // 判断传入的分页查询参数是否合法，至少不能小于等于 0。
        if(current <= 0 || size <= 0){
            return Result.build(HttpCode.INVALID_PARAMETER,"传入的分页参数为不合法");
        }
        // 获取传输的查询条件。
        String productCode = Objects.requireNonNull(productQueryVo).getProductCode();
        String productName = Objects.requireNonNull(productQueryVo).getProductName();
        Integer productType = Objects.requireNonNull(productQueryVo).getProductType();
        Integer stockNumber = Objects.requireNonNull(productQueryVo).getStockNumber();
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        // 产品编号、产品类型精确查询、产品规格和名称模糊查询、产品库存数量范围查询。
        wrapper.eq(StrUtil.isNotBlank(productCode),Product::getProductCode,productCode)
                .eq(productType != null && StrUtil.isNotBlank(String.valueOf(productType)),Product::getProductType,productType)
                .like(StrUtil.isNotBlank(productName),Product::getProductName,productName)
                .le(stockNumber != null && StrUtil.isNotBlank(String.valueOf(stockNumber)),Product::getStockNumber,stockNumber);
        Page<Product> productPage = productMapper.selectPage(new Page<>(current, size), wrapper);
        if(productPage == null){
            return Result.build(HttpCode.SUCCESS,"条件查询结果不存在");
        }

        // 查询成功返回分页结果信息。
        return Result.ok(productPage);
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里 URLEncoder.encode 可以防止中文乱码
            String fileName = URLEncoder.encode("产品数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 查询数据库中的数据。
            List<Product> defectList = productMapper.selectList(null);
            // 写出数据到浏览器端。
            EasyExcel.write(response.getOutputStream(), Product.class).sheet("产品数据").doWrite(defectList);
        }catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public Result<Object> importData(MultipartFile file) {
        ExcelListener<Product> listener = new ExcelListener<>(productMapper);
        // 调用 read 方法读取 excel 数据。
        try {
            EasyExcel.read(file.getInputStream(),
                    Product.class,
                    listener).sheet().doRead();
        } catch (IOException e) {
            throw new CustomException(HttpCode.FILE_IMPORT_FAILURE,"导入 Excel 数据失败");
        }
        return Result.build(HttpCode.SUCCESS, "导入 Excel 数据成功");
    }

    @Override
    public Result<Object> saveProduct(Product product) {
        if(product == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的产品对象不存在");
        }

        // 校验传输的产品名称。
        String productName = product.getProductName();
        if (StrUtil.isBlank(productName)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的产品名称为空");
        }
        // 校验传输的产品编号。
        String productCode = product.getProductCode();
        if (StrUtil.isBlank(productCode)) {
            String generateCode = PrefixGenerateUtil.generateCode(productName);
            product.setProductCode(generateCode);
        }else {
            Product quiredProduct = productMapper.getProductByCode(productCode);
            if (quiredProduct != null){
                return Result.build(HttpCode.ADD_FAILURE, "已存在产品编号对应的产品");
            }
        }
        // 校验传输的产品类型。
        Integer productType = product.getProductType();
        if (productType == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的产品类型为空");
        }else if (!ProduceType.isValidType(productType)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的产品类型不合法");
        }
        // 校验传输的产品规格。
        String productSize = product.getProductSize();
        if (StrUtil.isBlank(productSize)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的产品规格为空");
        }
        // 校验传输的库存数量。
        Integer stockNumber = product.getStockNumber();
        if (stockNumber == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的库存数量为空");
        }else if (stockNumber < 0){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的库存数量不合法");
        }
        // 校验传入的产品最小库存数目和最大库存数目。
        Integer productMinNumber = product.getProductMinNumber();
        Integer productMaxNumber = product.getProductMaxNumber();
        if (productMinNumber == null || productMaxNumber == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的产品最小库存数目和最大库存数目为空");
        }else if (stockNumber < productMinNumber || stockNumber > productMaxNumber){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的库存数量不合法");
        }
        // 校验传入的产品单位。
        String productUnit = product.getProductUnit();
        if (StrUtil.isBlank(productUnit)){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的产品单位为空");
        }

        // 校验通过则插入数据库中。
        productMapper.insert(product);
        return Result.build(HttpCode.SUCCESS, "添加产品成功");
    }

    @Override
    public Result<Object> updateProduct(Product product) {
        if(product == null){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的产品对象不存在");
        }
        // 校验传输的 id 是否存在：
        Long productId = product.getId();
        if(StrUtil.isBlank(String.valueOf(productId))){
            return Result.build(HttpCode.MODIFICATION_FAILURE,"修改产品时必须传输修改的产品 id");
        }else {
            // 校验产品 id 对应的产品对象是否存在。
            Product quiredProduct = productMapper.getProductById(productId);
            if(quiredProduct == null){
                return Result.build(HttpCode.MODIFICATION_FAILURE,"修改的产品对象不存在");
            }
        }

        // 校验通过则进行修改。
        productMapper.updateById(product);
        return Result.build(HttpCode.SUCCESS, "修改产品成功");
    }

    @Override
    public Result<Object> deleteProduct(Long productId) {
        // 校验传输的产品 id。
        if(StrUtil.isBlank(String.valueOf(productId))){
            return Result.build(HttpCode.UNPASSED_PARAMETER,"传入的删除的产品 id 不能为空");
        }else {
            // 校验产品 id 对应的产品对象是否存在。
            Product quiredProduct = productMapper.getProductById(productId);
            if(quiredProduct == null){
                return Result.build(HttpCode.MODIFICATION_FAILURE,"修改的产品对象不存在");
            }
        }

        // 校验是否有工单仍在使用产品，若有则不能删除。
        List<Long> workOrderIdList = workOrderAndProductMapper.getWorkIdByProductId(productId);
        if (CollUtil.isNotEmpty(workOrderIdList)){
            return Result.build(HttpCode.MODIFICATION_FAILURE,"该产品下有工单正在使用，不能删除");
        }

        // 校验通过则进行删除。
        productMapper.deleteById(productId);
        return Result.build(HttpCode.SUCCESS, "删除产品成功");
    }


}
