# 微服务脚手架

## 模块介绍

| 模块名称                 | 模块说明                                                                          |
|:--------------------:|:-----------------------------------------------------------------------------:|
| SpringCloud Gateway  | demo示例中，可查看[sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo) |
| 微服务聚合swagger         | demo示例中，可查看[sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo) |
| sakura-cache         | 提供`reids`操作工具类，分布式限流注解                                                        |
| ### sakura-common    | 公共通用组件，如自定义日志打印注解、单机限流注解、hutool包、excel操作工具类，apache-commons相关包、通用异常、公共返回类      |
| sakura-web           | web相关，sql盲注处理，MybatisPlus配置，动态修改日志等级接口，swagger+knife4j配置                      |
| sakura-db            | 数据库相关依赖，mysql连接依赖、MybatisPlus依赖、jpa依赖                                         |
| sakura-ms            | nacos、openfeign、discovery SpringCloud组件                                       |
| sakura-mq            | 基于rabbitmq的通用消息发布组件                                                           |
| sakura-flyway        | 初始化数据库，支持数据库脚本的版本管理                                                           |
| sakura-file-util     | fastdfs文件上传下载                                                                 |
| sakura-uid-generator | 分布式id                                                                         |
| sakura-minio         | minio文件操作                                                                     |
| sakura-cron          | 单机动态定时器配置                                                                     |
| sakura-es            | ElasticSearch通用查询模块                                                           |
| sakura-webSocket     | webSocket组件                                                                   |



## 脚手架使用demo

### [sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)



## 技术清单

| 技术                   | 版本             | 说明                    |
| -------------------- | -------------- | --------------------- |
| Spring Cloud         | Hoxton.SR12    | 快速构建分布式系统的框架          |
| Spring Cloud Alibaba | 2.2.7.RELEASE  | 阿里巴巴快速构建分布式系统的框架      |
| Nacos                | 2.0.3          | 发现、配置和管理微服务           |
| Spring Boot          | 2.3.12.RELEASE | 容器+MVC框架              |
| MybatisPlus          | 3.3.2          | ORM框架                 |
| Swagger-UI           | 3.0.0          | 文档生产工具                |
| knife4j              | 3.0.0          | 基于swagger更美观好用的文档UI   |
| RabbitMq             | 3.7.14         | 消息队列                  |
| Redis                | 6.0            | 分布式缓存                 |
| MySQL                | 8.0            | 关系型数据库                |
| easypoi              | 4.2.0          | 文档解析工具                |
| Druid                | 1.2.4          | 数据库连接池                |
| Lombok               | 1.18.6         | 简化对象封装工具              |
| fastdfs              | 1.26.2         | 分布式文件系统               |
| flyway               | 5.2.4          | 数据库版本管理工具             |
| hutool               | 5.5.7          | 常用工具类                 |
| uid-generator        | 1.0.3          | 百度开源的唯一id生成器，雪花算法的升级版 |
| minio                | 2021-06-17     | 文件服务器                 |
| ElasticSearch        | 7.16.3         | 搜索引擎                  |
| PowerJob             | 4.0.1          | 分布式调度                 |
