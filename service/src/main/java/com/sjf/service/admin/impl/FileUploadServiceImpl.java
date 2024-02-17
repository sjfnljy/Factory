package com.sjf.service.admin.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.sjf.common.Result;
import com.sjf.constant.HttpCode;
import com.sjf.exception.CustomException;
import com.sjf.service.admin.FileUploadService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Date;

/**
 * @Description: 文件上传 Service 实现类
 * @Author: SJF
 * @Date: 2024/1/15 16:44
 */

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Override
    public Result<String> upload(MultipartFile file) {
        try {
            // 判断指定的桶是否存在，不存在则创建桶。
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 生成存放文件的路径。
            String directory = DateUtil.format(new Date(), "yyyyMMdd");
            String fileName = directory + IdUtil.simpleUUID() +  file.getOriginalFilename();

            // 文件上传。
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            // 返回存储文件的地址。
            return Result.ok(endpoint + "/" + bucketName + "/" + fileName);
        }catch (Exception e) {
            throw new CustomException(HttpCode.FILE_UPLOAD_FAILURE, "文件" + file.getOriginalFilename() + "上传失败，原因为" + e.getMessage());
        }
    }
}
