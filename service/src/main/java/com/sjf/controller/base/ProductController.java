package com.sjf.controller.base;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.entity.base.Product;
import com.sjf.service.base.ProductService;
import com.sjf.vo.base.ProductQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 产品请求处理器
 * @Author: SJF
 * @Date: 2024/1/27 15:32
 */

@Api(tags = "基础数据产品管理接口")
@RestController
@RequestMapping("/factory/base/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping(value = "/exportData")
    @ApiOperation("导出产品数据")
    public void exportData(HttpServletResponse response) {
        productService.exportData(response);
    }

    @PostMapping("/importData")
    @ApiOperation("导入产品数据")
    public Result<Object> importData(MultipartFile file) {
        return  productService.importData(file);
    }

    @ApiOperation("查询所有产品")
    @GetMapping("/all")
    public Result<List<Product>> getAllProduct(){
        List<Product> productList = productService.list();
        if(CollUtil.isEmpty(productList)){
            return Result.build(HttpCode.SUCCESS,"查询结果不存在");
        }
        return Result.ok(productList);
    }

    @ApiOperation("条件查询并分页返回")
    @GetMapping("/page/{current}/{size}")
    public Result<Page<Product>> conditionQuery(@PathVariable("current") Integer current,
                                                @PathVariable("size") Integer size,
                                                ProductQueryVo productQueryVo){
        return productService.conditionQuery(current, size ,productQueryVo);
    }

    @ApiOperation("新增产品")
    @PostMapping("/save")
    public Result<Object> saveProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @ApiOperation("修改产品信息")
    @PutMapping("/update")
    public Result<Object> updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }

    @ApiOperation("根据产品 id 删除产品")
    @DeleteMapping("/delete/{productId}")
    public Result<Object> deleteProduct(@PathVariable("productId") Long productId){
        return productService.deleteProduct(productId);
    }
}
