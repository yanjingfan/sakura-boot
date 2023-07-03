# 微服务脚手架

## 模块介绍

| 模块名称                 | 模块说明                                                                     |
|:--------------------:|:------------------------------------------------------------------------:|
| sakura-cache         | 提供`reids`操作工具类，分布式限流注解                                                   |
| sakura-common        | 公共通用组件，如自定义日志打印注解、单机限流注解、hutool包、excel操作工具类，apache-commons相关包、通用异常、公共返回类 |
| sakura-cron          | 单机动态定时器配置                                                                |
| sakura-db            | 数据库相关依赖，mysql连接依赖、MybatisPlus依赖、jpa依赖                                    |
| sakura-es            | ElasticSearch通用查询模块                                                      |
| sakura-file-util     | fastdfs文件上传下载                                                            |
| sakura-flyway        | 初始化数据库，支持数据库脚本的版本管理                                                      |
| sakura-gateway       | 网关，集成kinfe4j，可统一通过网关请求各模块的在线文档                                           |
| sakura-loki          | Loki通用查询模块                                                               |
| sakura-minio         | minio文件操作                                                                |
| sakura-mq            | 基于rabbitmq的通用消息发布组件                                                      |
| sakura-ms            | nacos、seata、openfeign、discovery SpringCloud组件                            |
| sakura-oauth2        | 认证组件                                                                     |
| sakura-sa-token      | 认证组件（推荐这个，好用得一批）                                                         |
| sakura-seata         | 分布式事务                                                                    |
| sakura-uid-generator | 分布式id                                                                    |
| sakura-web           | web相关，sql盲注处理，MybatisPlus配置，动态修改日志等级接口，swagger+knife4j配置                 |
| sakura-web-socket    | webSocket组件                                                              |

## 开发计划

| 功能                   | 进度          |
| -------------------- | ----------- |
| 升级至springboot3 | 待开发         |
| Netty模块 | 开发中         |

## 脚手架使用demo

### [sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)

## 技术清单

| 技术                                                                                                      | 版本             | 说明                    |
| ------------------------------------------------------------------------------------------------------- | -------------- | --------------------- |
| [Spring Cloud](https://spring.io/projects/spring-cloud)                                                 | Hoxton.SR12    | 快速构建分布式系统的框架          |
| [Spring Cloud Alibaba](https://spring.io/projects/spring-cloud-alibaba)                                 | 2.2.8.RELEASE  | 阿里巴巴快速构建分布式系统的框架      |
| [Spring Security+OAuth2](https://spring.io/projects/spring-authorization-server)                        |                | 安全认证框架  |
| [Nacos](https://nacos.io/zh-cn/docs/quick-start.html)                                                   | 2.0.3          | 发现、配置和管理微服务           |
| [Spring Boot](https://spring.io/projects/spring-boot)                                                   | 2.3.12.RELEASE | 容器+MVC框架              |
| [MybatisPlus](https://baomidou.com/)                                                                    | 3.3.2          | ORM框架                 |
| [Swagger-UI](https://swagger.io/docs/)                                                                  | 3.0.0          | 文档生产工具                |
| [knife4j](https://doc.xiaominfo.com/)                                                                   | 3.0.0          | 基于swagger更美观好用的文档UI   |
| [RabbitMq](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#messaging.amqp) | 3.7.14         | 消息队列                  |
| [Redis](https://redis.io/documentation)                                                                 | 6.0            | 分布式缓存                 |
| [MySQL](http://www.deituicms.com/mysql8cn/cn/web.html)                                                  | 8.0            | 关系型数据库                |
| [easypoi](https://gitee.com/lemur/easypoi)                                                              | 4.2.0          | 文档解析工具                |
| [Lombok](https://projectlombok.org/features/all)                                                        | 1.18.6         | 简化对象封装工具              |
| [fastdfs](https://github.com/happyfish100/fastdfs)                                                      | 1.26.2         | 分布式文件系统               |
| [flyway](https://flywaydb.org/documentation/)                                                           | 5.2.4          | 数据库版本管理工具             |
| [hutool](https://hutool.cn/docs/#/)                                                                     | 5.8.5          | 常用工具类                 |
| [uid-generator](https://github.com/baidu/uid-generator)                                                 | 1.0.3          | 百度开源的唯一id生成器，雪花算法的升级版 |
| [minio](https://docs.min.io/)                                                                           | 2021-06-17     | 文件服务器                 |
| [ElasticSearch](https://www.elastic.co/guide/index.html)                                                | 7.16.3         | 搜索引擎                  |
| [PowerJob](https://github.com/PowerJob/PowerJob)                                                        | 4.0.1          | 分布式调度                 |
| [seata](https://seata.io/zh-cn/)                                                                        | 1.5.1          | 分布式框架                 |
| [satoken](https://sa-token.dev33.cn/)                                                                   | 1.30.0         | 认证框架                  |
| [loki](https://github.com/grafana/loki)                                                                 |                | 用来存储日志                |
| [websocket](https://spring.io/guides/gs/messaging-stomp-websocket/)                                     |                | 支持双向通信的协议             |
