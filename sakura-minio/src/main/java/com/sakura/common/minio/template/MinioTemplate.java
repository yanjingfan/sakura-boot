package com.sakura.common.minio.template;

import com.sakura.common.minio.config.FilesConfig;
import com.sakura.common.minio.item.MinioItem;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @auther yangfan
 * @date 2021/6/21
 * @describle Minio模板类
 */
@Component
@RequiredArgsConstructor
public class MinioTemplate implements InitializingBean {
    @Autowired
    private FilesConfig filesConfig;
    private MinioClient client;

    /**
     * 检查文件存储桶是否存在
     * @param bucketName
     * @return
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName){
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获取全部bucket
     * <p>
     * https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     */
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return client.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return client.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据bucketName删除信息
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public void removeBucket(String bucketName) {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 根据文件前缀查询文件
     *
     * @param bucketName bucket名称
     * @param fileName     文件夹名称
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    @SneakyThrows
    public List<MinioItem> getAllObjectsByPrefix(String bucketName, String fileName, boolean recursive) {
        List<MinioItem> objectList = new ArrayList<>();
        Iterable<Result<Item>> results =
                client.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(bucketName)
                                .prefix(fileName)
                                .recursive(recursive)
                                .build());
        for (Result<Item> result : results) {
            objectList.add(new MinioItem(result.get()));
        }
        return objectList;
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return url
     */
    @SneakyThrows
    public String getObjectURL(String bucketName, String objectName) {
        String url = client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
        return url;
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {
        InputStream stream = client.getObject(
                        GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        return stream;
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/octet-stream");
        client.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                        stream, stream.available(), -1)
                        .headers(headers)
                        .build());
    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contextType 类型
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) throws Exception {
        client.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                        stream, size, -1)
                        .contentType(contextType)
                        .build());
    }

    /**
     *
     * 创建文件夹
     *
     * @param bucketName bucketName
     * @param fileDir    创建在bucket下一个或多层级的文件夹，如：upload/img/，则创建为：/bucketName/upload/img/
     * @throws Exception
     */
    public void putObject(String bucketName, String fileDir) throws Exception {
        client.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(fileDir).stream(
                        new ByteArrayInputStream(new byte[] {}), 0, -1)
                        .build());
    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        StatObjectResponse statObject = client.statObject(StatObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
        return statObject;

    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectNames 文件名称集合
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String bucketName, List<String> objectNames) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        List<DeleteObject> objects = new LinkedList<>();
        objectNames.stream().forEach(objectName -> {
            DeleteObject deleteObject = new DeleteObject(objectName);
            objects.add(deleteObject);
        });
        Iterable<Result<DeleteError>> results =
                client.removeObjects(
                        RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            System.out.println(
                    "Error in deleting object " + error.objectName() + "; " + error.message());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.client = MinioClient.builder().endpoint(filesConfig.getEndpoint()).credentials(filesConfig.getAccessKey(), filesConfig.getSecretKey())
                .build();
    }
}
