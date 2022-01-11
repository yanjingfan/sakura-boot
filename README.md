# 微服务脚手架

## 技术清单

| 技术                   | 版本          | 说明                    |
| -------------------- | ----------- | --------------------- |
| Spring Cloud         | Hoxton.SR12 | 快速构建分布式系统的框架          |
| Spring Cloud Alibaba | 2.2.7       | 快速构建分布式系统的框架          |
| Nacos                | 2.0.3       | 发现、配置和管理微服务           |
| Spring Boot          | 2.1.9       | 容器+MVC框架              |
| MybatisPlus          | 3.3.2       | ORM框架                 |
| Swagger-UI           | 3.0.0       | 文档生产工具                |
| knife4j              | 3.0.0       | 基于swagger更美观好用的文档UI   |
| RabbitMq             | 3.7.14      | 消息队列                  |
| Redis                | 6.0         | 分布式缓存                 |
| MySQL                | 8.0         | 关系型数据库                |
| easypoi              | 4.2.0       | 文档解析工具                |
| Druid                | 1.2.4       | 数据库连接池                |
| Lombok               | 1.18.6      | 简化对象封装工具              |
| fastdfs              | 1.26.2      | 分布式文件系统               |
| flyway               | 5.2.4       | 数据库版本管理工具             |
| hutool               | 5.5.7       | 常用工具类                 |
| uid-generator        | 1.0.3       | 百度开源的唯一id生成器，雪花算法的升级版 |
| minio                | 2021-06-17  | 文件服务器                 |

## 计划与安排

| 模块                                      | 集成开发进度 |
| --------------------------------------- | ------ |
| SpringCloud Security + Gateway + Oauth2 | 待集成    |
| 微服务聚合swagger                            | 待集成    |
| aop日志公共模块                               | 已完成    |
| PowerJob分布式调度                           | 待集成    |
| sa-token                                | 待集成    |
| Redis + Lua 脚本实现分布式限流                   | 已完成    |
| Guava RateLimiter 实现单机版限流               | 已完成    |

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

在`controller`层使用此模块的`@RateLimiter`注解可实现限流

### sakura-common

> 公共组件

1. 加入依赖
   
   ```xml
   <dependency>
       <groupId>com.sakura</groupId>
       <artifactId>sakura-common</artifactId>
       <version>1.0</version>
   </dependency>
   ```

2. 通用异常类使用，只在业务层向外抛出
   
   [通用异常代码地址](https://github.com/yanjingfan/boot-parent/tree/master/sakura-common/src/main/java/com/sakura/common/exception)
   
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

3. 统一返回结构
   
   将MP查询到的分页信息转换使用`CommonPage`类的`restPage`方法
   
   返回给前端结果封装使用`com.sakura.common.result.CommonResult`类，
   
   ```java
   @GetMapping(value = "/users")
   public CommonResult<CommonPage<UserVO>> queryUsers() {
       IPage<UserVO> users = userService.queryUsers();
       return CommonResult.success(CommonPage.restPage(users));
   }
   ```

4. URL转码解码
   
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
   
   运行结果：
   
   ```txt
   url转码后的结果：http%3A%2F%2FurlCode
   url解码后的结果：http://urlCode
   ```

5. 加密解密
   
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
     
     运行结果：
     
     ```txt
     ===============base64======================
     base64加密后：c2FrdXJhLWRlbW8=
     base64解密后：sakura-demo
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
     
     运行结果：
     
     ```txt
     ===============testMD5======================
     md5加密后：01854bc4662d707a3a4000e87bac9a58
     ===============testsha256Hex======================
     sha256加密后：cb107ba0b5c0bca75cf6db4d5d72251cf8ed61b811e1a31035bc78d4779f2ba1
     ```

6. SpringBeanCtx
   
   [代码地址](https://github.com/yanjingfan/boot-parent/blob/master/sakura-common/src/main/java/com/sakura/common/utils/SpringBeanCtxUtils.java)

7. apache-common
   
   引入了apache的common工具包，无需自己引入

8. 文档导入导出使用easypoi
   
   [官方文档](http://doc.wupaas.com/docs/easypoi/)
   
   `什么场景该用哪个方法`
   
   ```txt
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

9. aop日志打印
   
   在`sakura-boot-demo`工程`controller`层的方法上，添加`@MyLog`注解，即可打印出请求的参数，方法，这个接口是否正常返回等日志信息

10. aop结合`Guava`的`RateLimiter`实现限流
    
    在`sakura-boot-demo`工程`controller`层的方法上，添加`@RateLimiter`注解，具体用法可参考[sakura-boot-demo/UserController.java](https://github.com/yanjingfan/sakura-boot-demo/blob/master/web/src/main/java/com/sakura/cloud/demo1/controller/UserController.java)类中的方法上的注解

### sakura-web

> web相关，sql盲注处理，MybatisPlus配置，动态修改日志等级接口，swagger+knife4j配置

### sakura-db

> mysql连接包、druid连接池、MP分页封装

1. 加入依赖

```xml
<parent>
    <groupId>com.sakura</groupId>
    <artifactId>sakura-db</artifactId>
    <version>1.0</version>
</parent>
```

2. MybatisPlus分页配置，扫描mapper文件的包路径为：`com.sakura.cloud.**.mapper*`
   
   代码地址：[MybatisPlusConfig.java](https://github.com/yanjingfan/boot-parent/blob/master/sakura-db/src/main/java/com/sakura/db/config/MybatisPlusConfig.java)

3. 添加数据库相关配置

```yaml
####################################################################################
###################################DataSource Config################################
####################################################################################
spring:
  datasource:
    # JDBC 配置
    type: com.alibaba.druid.pool.DruidDataSource
    #    schema:
    #      - classpath:/db/schema-mysql.sql
    #    data:
    #      - classpath:/db/data-mysql.sql
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.1.130:3306/sakura?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: admin
    druid:
      # 连接池配置
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      pool-prepared-statements: false
      max-pool-prepared-statement-per-connection-size: 20
      max-open-prepared-statements: 20
      validation-query: SELECT 'x' from dual
      validation-query-timeout: 30000
      test-on-borrow: false
      test-on-return: false
      test-while-idle: true
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      max-evictable-idle-time-millis: 300000
      filters: stat  

#sql日志打印
mybatis-plus:
# 会默认去mapper文件夹中找到xml文件
#  mapper-locations: classpath:/mapper/**/*Mapper.xml
  configuration:
    map-underscore-to-camel-case: true
    # 关闭二级缓存
    cache-enabled: false
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

### sakura-ms

> 集成了nacos，openfeign微服务组件

1. 加入依赖
   
   ```xml
   <parent>
       <groupId>com.sakura</groupId>
       <artifactId>sakura-ms</artifactId>
       <version>1.0</version>
   </parent>
   ```

2. feigin客户端（注意：使用时，需要在启动类上添加`@EnableFeignClients`注解）

3. bootstrap.yml配置
   
   ```yaml
   ############################################################################################
   ################################# 应用名称 与 配置远程配置仓库 ########################################
   ############################################################################################
   spring :
     application :
       name : web-demo
     cloud:
       nacos:
         config:
           server-addr: 216.240.130.167:8848
           namespace: cfcecf2f-dbdc-4801-8aad-bc67bc419384
           file-extension: yaml #获取的yaml格式的配置
         discovery:
           server-addr: 216.240.130.167:8848
           namespace: cfcecf2f-dbdc-4801-8aad-bc67bc419384
           register-enabled: true
     profiles:
       active: dev
   ```

### sakura-mq

> 通用消息发布、订阅工具

此模块需要新建消息内容和消息记录两张表，使用时引入`sakura-flyway`模块进行初始化，

建表sql脚本为[V1_1__init_20210510.sql](https://github.com/yanjingfan/sakura-boot/blob/master/sakura-mq/src/main/resources/db/migration/V1_1__init_20210510.sql)

### sakura-flyway

> 初始化数据库，支持数据库脚本的版本管理

`flyway`操作数据库，因此需要引入数据库驱动以及数据源依赖，所以配合`sakura-db`模块一起使用即可

1. 加入依赖
   
   + Flyway 依赖
     
     ```xml
     <parent>
         <groupId>com.sakura</groupId>
         <artifactId>sakura-flyway</artifactId>
         <version>1.0</version>
     </parent>
     ```
   
   + 初始化表结构，需要操作数据库，因此引入数据库驱动以及数据源依赖
     
     如果项目中不使用`sakura-db`模块，则需要引入相关依赖（这里用 spring-boot-starter-data-jdbc）
     
     ```xml
     <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-jdbc</artifactId>
     </dependency>
     
     <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <scope>runtime</scope>
     </dependency>
     ```

2. Flyway 知识补充
   
   + Flyway 默认会去读取 `classpath:db/migration`，可以通过 `spring.flyway.locations` 去指定自定义路径，多个路径使用半角英文逗号分隔，内部资源使用 `classpath:`，外部资源使用 `file:`
   
   + 如果项目初期没有数据库文件，但是又引用了 Flyway，那么在项目启动的时候，Flyway 会去检查是否存在 SQL 文件，此时你需要将这个检查关闭，`spring.flyway.check-location = false`
   
   + Flyway 会在项目初次启动的时候创建一张名为 `flyway_schema_history` 的表，在这张表里记录数据库脚本执行的历史记录，当然，你可以通过 `spring.flyway.table` 去修改这个值
   
   + Flyway 执行的 SQL 脚本必须遵循一种命名规则，`V<VERSION>__<NAME>.sql` 首先是 `V` ，然后是版本号，如果版本号有多个数字，使用`_`分隔，比如`1_0`、`1_1`，版本号的后面是 2 个下划线，最后是 SQL 脚本的名称。
     
     **这里需要注意：V 开头的只会执行一次，下次项目启动不会执行，也不可以修改原始文件，否则项目启动会报错，如果需要对 V 开头的脚本做修改，需要清空`flyway_schema_history`表，如果有个 SQL 脚本需要在每次启动的时候都执行，那么将 V 改为 `R` 开头即可**
   
   + Flyway 默认情况下会去清空原始库，再重新执行 SQL 脚本，这在生产环境下是不可取的，因此需要将这个配置关闭，`spring.flyway.clean-disabled = true`

3. 使用：参考[sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)工程
   
   + yml配置
     
     ```yaml
     spring:
       flyway:
         enabled: true
         # 迁移前校验 SQL 文件是否存在问题
         validate-on-migrate: true
         # 生产环境一定要关闭
         clean-disabled: true
         # 校验路径下是否存在 SQL 文件
         check-location: false
         # 最开始已经存在表结构，且不存在 flyway_schema_history 表时，需要设置为 true
         baseline-on-migrate: true
         # 基础版本 0
         baseline-version: 0
     ```
   
   + 初始化数据库脚本
     
     [V1_1__init_20210510.sql](https://github.com/yanjingfan/sakura-boot-demo/blob/master/src/main/resources/db/migration/V1_1__init_20210510.sql)

### sakura-file-util

> 集成了fastdfs文件上传下载工具类

### sakura-uid-generator

> 百度的开源ID生成算法。UidGenerator是Java实现的， 基于Snowflake算法的唯一ID生成器。UidGenerator以组件形式工作在应用项目中， 支持自定义workerId位数和初始化策略， 从而适用于docker等虚拟化环境下实例自动重启、漂移等场景。 在实现上， UidGenerator通过借用未来时间来解决sequence天然存在的并发限制； 采用RingBuffer来缓存已生成的UID, 并行化UID的生产和消费， 同时对CacheLine补齐，避免了由RingBuffer带来的硬件级「伪共享」问题. 最终单机QPS可达600万。 

### sukura-minio

> 集成了minio文件上传工具类

### sukura-cron

> 动态定时器配置
