package com.sjf.vo.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 维修记录条件查询传输类
 * @Author: SJF
 * @Date: 2023/9/12
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class RepairRecordQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String repairCode;

    private String repairPersonName;

    private BigDecimal repairCost;
}
