package top.buukle.opensource.generator.plus.service.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.buukle.login.cube.session.OperatorUserDTO;
import top.buukle.login.cube.session.SessionUtils;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.service.UploadService;
import top.buukle.opensource.generator.plus.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service("minioUploadServiceImpl")
@Slf4j
public class MinioUploadServiceImpl implements UploadService {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.domain}")
    private String domain;

    @Override
    public CommonResponse<String> uploadMultipartFile(MultipartFile file) {
        try {
            OperatorUserDTO operator = SessionUtils.getOperator();
            String originalFilename = file.getOriginalFilename();
            log.info("用户UID:{}开始上传文件:{},大小:{}",operator.getUserId(),originalFilename,file.getSize());
            String random = UUID.randomUUID().toString().replace(StringUtil.MIDDLE, StringUtil.EMPTY);
            String filename = random + StringUtil.BACKSLASH + originalFilename;
            // 上传
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            String fileId = StringUtil.BACKSLASH + bucket + StringUtil.BACKSLASH + filename;
            log.info("用户UID:{}上传文件完成:{},fileId:{}",operator.getUserId(),originalFilename,fileId);
            return new CommonResponse.Builder().buildSuccess(domain + "/" + fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResponse.Builder().buildFailedWithOriginMsg(e);
        }
    }

    @Override
    public CommonResponse<String> uploadFile(File file) {
        try {
            OperatorUserDTO operator = SessionUtils.getOperator();
            String originalFilename = file.getName();
            String random = UUID.randomUUID().toString().replace(StringUtil.MIDDLE, StringUtil.EMPTY);
            String filename = random + StringUtil.BACKSLASH + originalFilename;
            InputStream inputStream = new FileInputStream(file);
            // 上传
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .stream(inputStream, file.length(), -1)
                            .build()
            );
            String fileId = StringUtil.BACKSLASH + bucket + StringUtil.BACKSLASH + filename;
            log.info("用户UID:{}上传文件完成:fileId:{}",operator.getUserId(),fileId);
            return new CommonResponse.Builder().buildSuccess(domain + "/" + fileId);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResponse.Builder().buildFailedWithOriginMsg(e);
        }
    }

}
