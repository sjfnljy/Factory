package com.sjf.vo.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 不良品项查询传输类
 * @Author: SJF
 * @Date: 2024/1/19 16:26
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class DefectQueryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String defectCode;

    private String defectName;
}
