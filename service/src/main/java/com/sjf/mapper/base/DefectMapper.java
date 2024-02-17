package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.Defect;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;

/**
 * @Description: 不良品项类型 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/19 15:36
 */

@Resource
public interface DefectMapper extends BaseMapper<Defect> {
    Defect getDefectById(@Param("defect_id") Long defectId);

    Defect getDefectByName(@Param("defect_name")String defectName);

    Defect getDefectByCode(@Param("defect_code")String defectCode);

    void updateDefect(Defect defect);

    void deleteDefect(@Param("defect_id") Long defectId);
}
