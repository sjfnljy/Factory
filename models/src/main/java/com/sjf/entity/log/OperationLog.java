package com.sjf.entity.log;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sjf.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description: 操作日志记录实体类
 * @Author: SJF
 * @Date: 2024/1/19 20:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tb_log_oper_log")
public class OperationLog extends BaseEntity {

    @ApiModelProperty("模块标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("方法名称")
    @TableField("method")
    private String method;

    @ApiModelProperty("请求方式")
    @TableField("request_method")
    private String requestMethod;

    @ApiModelProperty("操作类别（0其它 1后台用户 2手机端用户）")
    @TableField("operator_type")
    private String operatorType;

    @ApiModelProperty("业务类型（0查询 1新增 2修改 3删除）")
    @TableField("business_type")
    private Integer businessType ;

    @ApiModelProperty("操作人员")
    @TableField("oper_name")
    private String operationName;

    @ApiModelProperty("请求 URL")
    @TableField("oper_url")
    private String operationUrl;

    @ApiModelProperty("主机地址")
    @TableField("oper_ip")
    private String operationIp;

    @ApiModelProperty("请求参数")
    @TableField("oper_param")
    private String operationParam;

    @ApiModelProperty("返回参数")
    @TableField("json_result")
    private String jsonResult;

    @ApiModelProperty("操作状态（0正常 1异常）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("错误消息")
    @TableField("error_msg")
    private String errorMsg;
}
