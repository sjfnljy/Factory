package com.sjf.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjf.entity.base.Defect;
import com.sjf.entity.base.ProcessAndDefect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 工序和不良品项中间表 Mapper 接口
 * @Author: SJF
 * @Date: 2024/1/20 16:15
 */

@Repository
public interface ProcessAndDefectMapper extends BaseMapper<ProcessAndDefect> {

    List<Long> getProcessIdByDefectId(@Param("defect_id") Long defectId);

    List<Long> getDefectIdsByProcessId(@Param("process_id") Long processId);

    List<Defect> getDefectByProcessId(@Param("process_id") Long processId);

    void deleteByProcessId(@Param("process_id") Long processId);
}
