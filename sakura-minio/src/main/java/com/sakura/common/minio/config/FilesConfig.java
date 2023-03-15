package com.sakura.common.minio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @auther yangfan
 * @date 2021/6/21
 * @describle minio读取文件的配置
 */
@Component
@Data
@ConfigurationProperties("minio")
public class FilesConfig {

    /**
     * minio的路径
     */
    private String endpoint;

    /**
     * minio的accessKey
     */
    private String accessKey;

    /**
     * minio的secretKey
     */
    private String secretKey;

    /**
     * 下载地址
     */
    private String httpUrl;

    /**
     * 图片大小限制
     */
    private Long imgSize;

    /**
     * 文件大小限制
     */
    private Long fileSize;
}
