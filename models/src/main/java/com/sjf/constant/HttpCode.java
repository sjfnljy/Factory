package com.sjf.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 后端响应状态码常量
 * @Author: SJF
 * @Date: 2024/1/7 21:47
 */
@Data
@ApiModel(value = "响应结果实体类")
public class HttpCode {

    @ApiModelProperty(value = "成功返回 200")
    public final static int SUCCESS = 200;

    @ApiModelProperty(value = "失败返回 400")
    public final static int FAILURE = 400;

    @ApiModelProperty(value = "认证失败返回 406")
    public final static int AUTHENTICATION_FAILURE = 406;

    @ApiModelProperty(value = "授权失败返回 407")
    public final static int AUTHORIZATION_FAILURE = 407;

    @ApiModelProperty(value = "文件上传失败返回 408")
    public final static int FILE_UPLOAD_FAILURE = 408;

    @ApiModelProperty(value = "文件导入失败返回 409")
    public final static int FILE_IMPORT_FAILURE = 409;

    @ApiModelProperty(value = "未传递参数返回 410")
    public final static int UNPASSED_PARAMETER = 410;

    @ApiModelProperty(value = "参数不合理返回 420")
    public final static int INVALID_PARAMETER = 420;

    @ApiModelProperty(value = "添加失败返回 430")
    public final static int ADD_FAILURE = 430;

    @ApiModelProperty(value = "查询失败返回 440")
    public final static int QUERY_FAILURE = 440;

    @ApiModelProperty(value = "修改失败返回 450")
    public final static int MODIFICATION_FAILURE = 450;

    @ApiModelProperty(value = "删除失败返回 460")
    public final static int DELETION_FAILURE = 460;
}
