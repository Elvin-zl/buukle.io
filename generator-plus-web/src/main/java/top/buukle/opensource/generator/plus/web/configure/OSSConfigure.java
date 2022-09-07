package top.buukle.opensource.generator.plus.web.configure;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OSSConfigure {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.credentials.access-key}")
    private String accessKey;
    @Value("${minio.credentials.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        log.info("开始初始化minio客户端,endpoint:{}",endpoint);
        MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        log.info("初始化minio客户端完成,endpoint:{}",endpoint);
        return minioClient;
    }
}
