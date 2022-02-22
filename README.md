# 微服务脚手架

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

## 计划与安排

| 模块                        | 集成开发进度                                                                    |
| ------------------------- | ------------------------------------------------------------------------- |
| SpringCloud Gateway       | 已完成，可查看[sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo) |
| 微服务聚合swagger              | 已完成，可查看[sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo) |
| aop日志公共模块                 | 已完成                                                                       |
| PowerJob分布式调度             | 已集成                                                                       |
| sa-token                  | 待集成                                                                       |
| Redis + Lua 脚本实现分布式限流     | 已完成                                                                       |
| Guava RateLimiter 实现单机版限流 | 已完成                                                                       |
| ElasticSearch通用简单查询模块     | 已完成                                                                       |
| kkFileView文件预览 | 待集成 |

## 脚手架使用demo

[sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)

## 脚手架依赖说明

### root.pom

> 顶级pom，确定各种依赖包的版本，这样的话，子pom引入包不需要添加版本号

添加依赖

```xml
<parent>
    <artifactId>sakura-boot</artifactId>
    <groupId>com.sakura</groupId>
    <version>1.0-SNAPSHOT</version>
</parent>
```

### sakura-cache

> 提供`reids`缓存操作

引入此依赖后，在`controller`层使用此模块的`@RateLimiter`注解可实现限流

### sakura-common

> 公共组件

1. 常用功能
   
   - 日志打印
     
     在`controller`层的方法上，添加`@MyLog`注解，即可打印出请求的参数，方法，这个接口是否正常返回等日志信息
   
   - aop结合`Guava`的`RateLimiter`实现单机限流
     
     在`controller`层的方法上，添加`@RateLimiter`注解，具体用法可参考[sakura-boot-demo/UserController.java](https://github.com/yanjingfan/sakura-boot-demo/blob/master/web/src/main/java/com/sakura/cloud/demo1/controller/UserController.java)类中的方法上的注解
   + 通用异常类，如`YErrorException`、`CloudException`等，用法如下
     
     ```java
     @Override
     public void saveUser(UserDTO userDTO) {
         try {
            ....
            if (result == 0) {
                    throw new YErrorException("添加用户失败！");
            }
         } catch (Exception e) {
             //未知异常，通用处理
             throw new CloudException("用户插入出错！", e);
         }
     }
     ```
   
   + 公共返回类
     
     分页结果集封装：`CommonPage.restPage`自定义分页处理
     
     公共返回：使用`com.sakura.common.result.CommonResult`相关API
     
     用法如下
     
     ```java
     @GetMapping(value = "/users")
     public CommonResult<CommonPage<UserVO>> queryUsers() {
         IPage<UserVO> users = userService.queryUsers();
         return CommonResult.success(CommonPage.restPage(users));
     }
     ```
   
   + URL转码解码
     
     ```java
     @Test
     public void testURLCodec() throws Exception {
         URLCodec codec = new URLCodec();
         String data = "http://urlCode";
         //转码
         String encode = codec.encode(data, "UTF-8");
         System.out.println("url转码后的结果：" + encode);
         //解码
         String decode = codec.decode(encode, "UTF-8");
         System.out.println("url解码后的结果：" + decode);
     }
     ```
   
   + 加密解密
     
     + Base64
       
       ```java
       @Test
       public void testBase64() {
           System.out.println("===============base64======================");
           byte[] data = "sakura-demo".getBytes();
           Base64 base64 = new Base64();
           //转码
           String encode = base64.encodeAsString(data);
           System.out.println("base64加密后：" + encode);
           //解码
           String decode = new String(base64.decode(encode));
           System.out.println("base64解密后：" + decode);
       }
       ```
     
     + 使用加密工具类DigestUtils
       
       ```java
       /**
        * DigestUtils工具类里有多种加密的方式，自行选择
        */
       @Test
       public void testDigestUtils() {
           System.out.println("===============testMD5======================");
           String result = DigestUtils.md5Hex("sakura-demo-md5");
           System.out.println("md5加密后：" + result);
       
           System.out.println("===============testsha256Hex======================");
           String sha256Hex = DigestUtils.sha256Hex("sakura-demo-sha256");
           System.out.println("sha256加密后：" + sha256Hex);
       }
       ```
   
   + 第三方工具类，如commons-lang3、commons-collections、commons-io、hutool、mapstruct、easypoi等
     
     + [easypoi官方文档](http://doc.wupaas.com/docs/easypoi/)
       
        `什么场景该用哪个方法`
       
       ```textile
        - 导出
        1.正规excel导出 (格式简单,数据量可以,5W以内吧)
        注解方式:  ExcelExportUtil.exportExcel(ExportParams entity, Class<?> pojoClass,Collection<?> dataSet) 
        2.不定多少列,但是格式依然简单数据库不大
        自定义方式: ExcelExportUtil.exportExcel(ExportParams entity, List<ExcelExportEntity> entityList,Collection<?> dataSet)
        3.数据量大超过5W,还在100W以内
        注解方式 ExcelExportUtil.exportBigExcel(ExportParams entity, Class<?> pojoClass,IExcelExportServer server, Object queryParams)
        自定义方式: ExcelExportUtil.exportBigExcel(ExportParams entity, List<ExcelExportEntity> excelParams,IExcelExportServer server, Object queryParams)
        4.样式复杂,数据量尽量别大
        模板导出 ExcelExportUtil.exportExcel(TemplateExportParams params, Map<String, Object> map)
        5.一次导出多个风格不一致的sheet
        模板导出 ExcelExportUtil.exportExcel(Map<Integer, Map<String, Object>> map,TemplateExportParams params) 
        6.一个模板但是要导出非常多份
        模板导出 ExcelExportUtil.exportExcelClone(Map<Integer, List<Map<String, Object>>> map,TemplateExportParams params)
        7.模板无法满足你的自定义,试试html
        自己构造html,然后我给你转成excel  ExcelXorHtmlUtil.htmlToExcel(String html, ExcelType type)
        8.数据量过百万级了.放弃excel吧,csv导出
        注解方式: CsvExportUtil.exportCsv(CsvExportParams params, Class<?> pojoClass, OutputStream outputStream)
        自定义方式: CsvExportUtil.exportCsv(CsvExportParams params, List<ExcelExportEntity> entityList, OutputStream outputStream) 
        9.word导出
        模板导出: WordExportUtil.exportWord07(String url, Map<String, Object> map)
       
        - 导入 
        如果想提高性能 ImportParams 的concurrentTask 可以帮助并发导入,仅单行,最小1000
        excel有单个的那种特殊读取,readSingleCell 参数可以支持
        1. 不需要检验,数据量不大(5W以内)
        注解或者MAP: ExcelImportUtil.importExcel(File file, Class<?> pojoClass, ImportParams params)
        2. 需要导入,数据量不大
        注解或者MAP: ExcelImportUtil.importExcelMore(InputStream inputstream, Class<?> pojoClass, ImportParams params)
        3. 数据量大了,或者你有特别多的导入操作,内存比较少,仅支持单行
        SAX方式  ExcelImportUtil.importExcelBySax(InputStream inputstream, Class<?> pojoClass, ImportParams params, IReadHandler handler)
        4. 数据量超过EXCEL限制,CSV读取
        小数据量: CsvImportUtil.importCsv(InputStream inputstream, Class<?> pojoClass,CsvImportParams params)
        大数据量: CsvImportUtil.importCsv(InputStream inputstream, Class<?> pojoClass,CsvImportParams params, IReadHandler readHandler)
       ```

### sakura-web

> web相关，sql盲注处理，MybatisPlus配置，动态修改日志等级接口，swagger+knife4j配置

### sakura-db

> 数据库相关依赖，mysql连接依赖、MybatisPlus依赖、jpa依赖

+ MybatisPlus分页配置，扫描mapper文件的包路径为`com.sakura.cloud.**.mapper*`
  
  包扫描路径修改：[MybatisPlusConfig.java](https://github.com/yanjingfan/boot-parent/blob/master/sakura-db/src/main/java/com/sakura/db/config/MybatisPlusConfig.java)

### sakura-ms

> 集成了nacos，openfeign、gateway微服务组件

+ feign客户端（注意：使用时，需要在启动类上添加`@EnableFeignClients`注解）

### sakura-mq

> 通用消息发布、订阅工具

此模块需要新建消息内容和消息记录两张表，使用时引入`sakura-flyway`模块进行初始化，

建表sql脚本为[V2021010101__sakura-mq-1.0.0_初始化脚本.sql](https://github.com/yanjingfan/sakura-boot-demo/blob/master/web/src/main/resources/db/migration/V2021010101__sakura-mq-1.0.0_%E5%88%9D%E5%A7%8B%E5%8C%96%E8%84%9A%E6%9C%AC.sql)

### sakura-flyway

> 初始化数据库，支持数据库脚本的版本管理

`flyway`初始化数据库，需要引入数据库驱动以及数据源依赖，配合`sakura-db`模块一起

引入即可

1. `Flyway`知识
   
   + Flyway 默认会去读取 `classpath:db/migration`，可以通过 `spring.flyway.locations` 去指定自定义路径，多个路径使用半角英文逗号分隔，内部资源使用 `classpath:`，外部资源使用 `file:`
   
   + 如果项目初期没有数据库文件，但是又引用了 Flyway，那么在项目启动的时候，Flyway 会去检查是否存在 SQL 文件，此时你需要将这个检查关闭，`spring.flyway.check-location = false`
   
   + Flyway 会在项目初次启动的时候创建一张名为 `flyway_schema_history` 的表，在这张表里记录数据库脚本执行的历史记录，当然，你可以通过 `spring.flyway.table` 去修改这个值
   
   + Flyway 执行的 SQL 脚本必须遵循一种命名规则，`V<VERSION>__<NAME>.sql` 首先是 `V` ，然后是版本号，如果版本号有多个数字，使用`_`分隔，比如`1_0`、`1_1`，版本号的后面是 2 个下划线，最后是 SQL 脚本的名称。
     
     **这里需要注意：V 开头的只会执行一次，下次项目启动不会执行，也不可以修改原始文件，否则项目启动会报错，如果需要对 V 开头的脚本做修改，需要清空`flyway_schema_history`表，如果有个 SQL 脚本需要在每次启动的时候都执行，那么将 V 改为 `R` 开头即可**
   
   + Flyway 默认情况下会去清空原始库，再重新执行 SQL 脚本，这在生产环境下是不可取的，因此需要将这个配置关闭，`spring.flyway.clean-disabled = true`

### sakura-file-util

> 集成了fastdfs文件上传下载工具类

### sakura-uid-generator

> 百度的开源分布式ID生成器 

[baidu/uid-generator: UniqueID generator (github.com)](https://github.com/baidu/uid-generator)

使用此模块生成分布式id，需要先建一张记录表，每次重启项目都会生成一条记录

建表sql脚本：[V2021010102__sakura-uid-generator-1.0.0_初始化脚本.sql](https://github.com/yanjingfan/sakura-boot-demo/blob/master/web/src/main/resources/db/migration/V2021010102__sakura-uid-generator-1.0.0_%E5%88%9D%E5%A7%8B%E5%8C%96%E8%84%9A%E6%9C%AC.sql)

### sakura-minio

> 集成了minio文件上传工具类

### sakura-cron

> 动态定时器配置

+ 使用单机调度时，需要初始化一张任务记录表，记录任务的开启状态，cron表达式等等

        建表sql脚本：[V2021101801__sakura-cron-1.0.0_动态定时任务.sql](https://github.com/yanjingfan/sakura-boot-demo/blob/master/web/src/main/resources/db/migration/V2021101801__sakura-cron-1.0.0_%E5%8A%A8%E6%80%81%E5%AE%9A%E6%97%B6%E4%BB%BB%E5%8A%A1.sql)

+ 使用`PowerJob`分布式调度时，先看看官网使用文档：[PowerJob · 语雀 ](https://www.yuque.com/powerjob/guidence/intro)

### sakura-es

> ElasticSearch通用查询模块
