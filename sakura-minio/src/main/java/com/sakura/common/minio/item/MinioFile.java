package com.sakura.common.minio.item;

import lombok.Data;

/**
 * @auther yangfan
 * @date 2021/6/22
 * @describle
 */

@Data
public class MinioFile {

    private String fileName;

    private String bucketName;

    private String fileUrl;

}
