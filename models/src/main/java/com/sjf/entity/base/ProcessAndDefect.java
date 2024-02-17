package com.sjf.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 工序和不良品项中间表实体类
 * @Author: SJF
 * @Date: 2024/1/20
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "tb_base_process_defect")
public class ProcessAndDefect extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableField("process_id")
    private Long processId;

    @TableField("defect_id")
    private Long defectId;
}
