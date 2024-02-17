package com.sjf.controller.admin;

import com.sjf.common.Result;
import com.sjf.service.admin.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Description: 文件上传请求处理器
 * @Author: SJF
 * @Date: 2024/1/15 16:17
 */

@Api(tags = "文件上传管理接口")
@RestController
@RequestMapping("/factory/admin/file")
public class FileUploadController {

    @Resource
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    @ApiOperation(value = "上传指定文件")
    public Result<String> upload(MultipartFile file) {
        return fileUploadService.upload(file);
    }
}
