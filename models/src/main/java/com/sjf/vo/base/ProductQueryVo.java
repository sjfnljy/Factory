package com.sjf.vo.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 产品条件查询传输类
 * @Author: SJF
 * @Date: 2024/1/27
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productCode;

    private String productName;

    private Integer productType;

    private Integer stockNumber;
}
