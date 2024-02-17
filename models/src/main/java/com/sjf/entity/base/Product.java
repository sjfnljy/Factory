package com.sjf.entity.base;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 工厂产品实体类
 * @Author: SJF
 * @Date: 2024/1/27
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_product")
public class Product extends BaseEntity {

    @TableField("product_code")
    @ApiModelProperty("产品编号")
    @ExcelProperty(value = "产品编号", index = 0)
    private String productCode;

    @TableField("product_name")
    @ApiModelProperty("产品名称")
    @ExcelProperty(value = "产品名称", index = 1)
    private String productName;

    @TableField("product_size")
    @ApiModelProperty("产品尺寸")
    @ExcelProperty(value = "产品尺寸", index = 2)
    private String productSize;

    @TableField("product_type")
    @ApiModelProperty("产品类型")
    @ExcelProperty(value = "产品类型", index = 3)
    private Integer productType;

    @TableField("product_min_number")
    @ApiModelProperty("产品库存最小数量")
    @ExcelProperty(value = "产品库存最小数量", index = 4)
    private Integer productMinNumber;

    @TableField("product_max_number")
    @ApiModelProperty("产品库存最大数量")
    @ExcelProperty(value = "产品库存最大数量", index = 5)
    private Integer productMaxNumber;

    @TableField("stock_number")
    @ApiModelProperty("库存数量")
    @ExcelProperty(value = "库存数量", index = 6)
    private Integer stockNumber;

    @TableField("product_unit")
    @ApiModelProperty("产品单位")
    @ExcelProperty(value = "产品单位", index = 7)
    private String productUnit;
}
