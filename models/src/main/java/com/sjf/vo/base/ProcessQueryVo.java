package com.sjf.vo.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 工序条件查询传输类
 * @Author: SJF
 * @Date: 2024/1/23
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessQueryVo implements Serializable {

    private String processName;

    private String processCode;

    private Integer processRatio;

    private Integer processStatus;
}
