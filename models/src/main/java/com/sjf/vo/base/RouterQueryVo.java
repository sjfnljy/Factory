package com.sjf.vo.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description: 工艺路线条件查询传输类
 * @Author: SJF
 * @Date: 2023/9/7
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class RouterQueryVo implements Serializable {

    private String routerCode;

    private String routerName;
}
