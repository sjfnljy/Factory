package com.sjf.service.admin;

import com.sjf.common.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 文件上传 Service 接口
 * @Author: SJF
 * @Date: 2024/1/15 16:43
 */

public interface FileUploadService {
    /**
     * 上传指定的文件到 minio 并返回文件存储地址
     * @param file: 指定的文件
     * @return: 文件存储地址
     */
    Result<String> upload(MultipartFile file);
}
