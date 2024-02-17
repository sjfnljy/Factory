package com.sjf.mapper.produce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.produce.WorkOrderAndProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 工单和产品中间表管理 Mapper 接口
 * @Author: SJF
 * @Date: 2024/2/7 17:21
 */

@Repository
public interface WorkOrderAndProductMapper extends BaseMapper<WorkOrderAndProduct> {
    void deleteWorkOrderAndProduct(@Param("work_order_id") Long workOrderId);

    List<Long> getWorkIdByProductId(@Param("product_id") Long productId);
}
