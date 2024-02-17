package com.sjf.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 工艺路线和工序中间表实体类
 * @Author: SJF
 * @Date: 2024/1/24 21:37
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_router_process")
public class RouterAndProcess extends BaseEntity {

    @TableField("router_id")
    private Long routerId;

    @TableField("process_id")
    private Long processId;

    public RouterAndProcess(Long routerId, Long processId) {
        this.routerId = routerId;
        this.processId = processId;
    }
}
