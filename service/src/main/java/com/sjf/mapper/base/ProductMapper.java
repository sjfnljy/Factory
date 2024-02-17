package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description: 产品管理 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/27 15:32
 */

@Repository
public interface ProductMapper extends BaseMapper<Product> {

    Product getProductByCode(@Param("product_code") String productCode);

    Product getProductById(@Param("product_id") Long productId);
}
