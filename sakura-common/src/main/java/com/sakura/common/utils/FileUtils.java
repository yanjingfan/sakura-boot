package com.sakura.common.utils;

import com.sakura.common.exception.YErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @auther yangfan
 * @date 2022/1/10
 * @describle 文件相关操作
 */
public class FileUtils {

    /**
     * 从远程网络下载大文件到本地
     * @param remoteFileUrl 远程文件url，参数如：https://github.com/kekingcn/kkFileView/archive/refs/tags/v4.0.0.zip
     * @param localFilePath 下载到本地的带文件名的地址。参数如：D:/home/chinaemt/im/v4.0.0.zip
     * @return
     */
    public static String downLoadFromUrl(String remoteFileUrl, String localFilePath) {
        RestTemplate restTemplate = new RestTemplate();

        if (StringUtils.isBlank(remoteFileUrl)) {
            throw new YErrorException("远程文件地址不能为空!");
        }

        File serverFile = new File(localFilePath);
        if (!serverFile.getParentFile().exists()) {
            serverFile.getParentFile().mkdirs();
        }

        // 待下载的文件地址
//        String fileUrl = "https://github.com/kekingcn/kkFileView/archive/refs/tags/v4.0.0.zip";
        // 文件保存的本地路径
//        String filePath = "D:/home/chinaemt/im/v4.0.0.zip";
        //定义请求头的接收类型

        RequestCallback requestCallback = request -> request.getHeaders()
                .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
        //对响应进行流式处理而不是将其全部加载到内存中
        restTemplate.execute(remoteFileUrl, HttpMethod.GET, requestCallback, clientHttpResponse -> {
            Files.copy(clientHttpResponse.getBody(), Paths.get(localFilePath));
            return null;
        });
        return localFilePath;
    }

}
