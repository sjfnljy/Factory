package com.sjf.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 工艺路线分配工序传输类
 * @Author: SJF
 * @Date: 2024/1/25 15:55
 */

@Data
@ApiModel(description = "工艺路线分配工序")
public class RouterAssignProcessVo implements Serializable {

    @ApiModelProperty(value = "工艺路线 id")
    private Long routerId;

    @ApiModelProperty(value = "工序 id 列表")
    private List<Long> processIdList;
}
