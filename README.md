# 微服务脚手架

## 技术清单	

| 技术         | 版本          | 说明                             |
| ------------ | ------------- | -------------------------------- |
| Spring Cloud | Greenwich.SR6 | 快速构建分布式系统的框架         |
| Spring Boot  | 2.1.9         | 容器+MVC框架                     |
| MybatisPlus  | 3.3.2         | ORM框架                          |
| Swagger-UI   | 3.0.0         | 文档生产工具                     |
| knife4j      | 3.0.0         | 基于swagger更美观好用的文档UI    |
| RabbitMq     | 3.7.14        | 消息队列                         |
| Redis        | 6.0           | 分布式缓存                       |
| MySQL        | 8.0           | 关系型数据库                     |
| easypoi      | 4.2.0         | 文档解析工具                     |
| Druid        | 1.2.4         | 数据库连接池                     |
| Lombok       | 1.18.6        | 简化对象封装工具                 |
| fastdfs      | 1.26.2        | 分布式文件系统                   |
| flyway       |               | 数据库版本管理工具（待集成开发） |
| hutool       | 5.5.7         | 常用工具类                       |

## 脚手架依赖说明

### root.pom

> 顶级pom，确定各种依赖包的版本，这样的话，子pom引入包不需要添加版本号

添加依赖

```xml
<parent>
    <artifactId>boot-parent</artifactId>
    <groupId>com.sakura</groupId>
    <version>1.0-SNAPSHOT</version>
</parent>
```



### sakura-cache

> 通用缓存操作工具包

加入依赖

```xml
<dependency>
    <groupId>com.sakura</groupId>
    <artifactId>sakura-cache</artifactId>
    <version>1.0</version>
</dependency>
```

提供`reids`缓存操作

工具类：

相关配置

```yaml
spring
  redis:
    database: 0           # Redis数据库索引（默认为0）
    host: 192.168.1.130     # Redis服务器地址
    port: 16377            # Redis服务器连接端口
    password: 37621040      #Redis服务器连接密码（默认为空）
    timeout: 5000ms            # 连接超时时间（毫秒）
    lettuce:				# 使用默认的redis连接池
      pool:
        max-idle: 8         # 连接池中的最大空闲连接
        min-idle: 1         # 连接池中的最小空闲连接
        max-active: 8       # 连接池最大连接数（使用负值表示没有限制）
        max-wait: -1ms        # 连接池最大阻塞等待时间（使用负值表示没有限制）
```



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

   将MP查询到的分页信息转换使用`com.sakura.db.mp.CommonPage`类的`restPage`方法

   返回给前端结果封装使用`com.sakura.common.result.CommonResult`类，

   ```java
   @GetMapping(value = "/users")
   public CommonResult<CommonPage<UserVO>> queryUsers() {
       try {
           IPage<UserVO> users = userService.queryUsers();
           return CommonResult.success(CommonPage.restPage(users));
       } catch(CloudException e) {
           log.error(e.getMessage(), e);
           //返回失败信息给前端
           return CommonResult.failed(e.getMessage());
       } catch (Exception e) {
           //未知异常
           log.error(e.getMessage(), e);
           return CommonResult.failed();
       }
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



### sakura-web

> web相关

1. 加入依赖

   ```xml
   <parent>
       <groupId>com.sakura</groupId>
       <artifactId>sakura-web</artifactId>
       <version>1.0</version>
   </parent>
   ```

2. 集成了Swagger-ui

   `swagger-ui`的配置类：[https://github.com/yanjingfan/boot-parent/blob/master/sakura-web/src/main/java/com/sakura/common/web/config/Swagger2Config.java](https://github.com/yanjingfan/boot-parent/blob/master/sakura-web/src/main/java/com/sakura/common/web/config/Swagger2Config.java)

   **注意：**新建工程的包结构为：`com.sakura.cloud`

   集成后，启动项目后的访问地址：

   [http://localhost:8080/doc.html](http://localhost:8080/doc.html)

   或

   [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

3. 集成了SQL防注入、XXS、CSRF

   + 关闭csrf检查

     如果不需要CSRF，可配置关闭

     ```yaml
     # 信息安全
     security:
       csrf:
         enable: false   # false表示关闭，true为打开，默认打开
         excludes:
           - /img		#忽略检查的url
           - /js
     ```

   + 关闭sql盲注

     如果不需要sql盲注拦截，可配置关闭

     ```yaml
     # 信息安全
     security:
       sql:
         enable: false   # false表示关闭，true为打开，默认打开
         excludes:
           - /img		#忽略检查的url
           - /js
     ```




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

   代码地址：[https://github.com/yanjingfan/boot-parent/blob/master/sakura-db/src/main/java/com/sakura/db/config/MybatisPlusConfig.java](https://github.com/yanjingfan/boot-parent/blob/master/sakura-db/src/main/java/com/sakura/db/config/MybatisPlusConfig.java)

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

> SpringCloud

1. 加入依赖

   ```xml
   <parent>
       <groupId>com.sakura</groupId>
       <artifactId>sakura-ms</artifactId>
       <version>1.0</version>
   </parent>
   ```

2. eureka客户端

3. config客户端

4. feigin客户端（注意：需要在启动类上添加`@EnableFeignClients`注解）

```yaml
############################################################################################
################################# 应用名称 与 配置远程配置仓库 ##############################################################################################
spring :
  authentication: true
  application :
    name : sakura-demo                # 本项目的服务名
  cloud:
    config:
      profile: dev
      label: master
      discovery:
        enabled: false                 # 生产环境为true，开发环境可为false
        service-id: ly-cloud-config-server        # config的服务名
############################################################################################
###################################### 运行容器端口设置 ##########################################
############################################################################################
server :
  port : 8080
  tomcat :
   uri-encoding : UTF-8
############################################################################################
################################### eureka Server ##########################################
############################################################################################
eureka :
  instance :
    prefer-ip-address : true
    lease-renewal-interval-in-seconds : 30
    lease-expiration-duration-in-seconds : 90
  client :
    service-url :
      defaultZone : ${defaultZone:http://${discovery:discovery}:1200/eureka/}
    register-with-eureka : false            # 生产环境为true，开发环境可为false       
    fetch-registry : false                  # 生产环境为true，开发环境可为false
```

### sakura-mq

> 通用消息发布、订阅工具

加入依赖

```xml
<parent>
    <groupId>com.sakura</groupId>
    <artifactId>sakura-mq</artifactId>
    <version>1.0</version>
</parent>
```

引入此模块需要在`application.yml`添加`rabbitmq`和`redis`的相关配置

+ 可以根据具体情况，新建mq的用户、密码、虚拟主机

```yaml
spring:
  rabbitmq:
    host: 192.168.1.130
    port: 5672
    virtual-host: /mall
    username: admin
    password: admin
    listener:
      simple:
        acknowledge-mode: manual #开启手动Ack。消息默认设置为自动ack，这种情况下，MQ只要确认消息发送成功，无须等待应答就会丢弃消息
        prefetch: 2
```



发布和订阅：

1、注入事件操作类

```java
@Autowired
private Event event;
```



2、事件发布

```java
//填写事件的相关配置和内容
DefaultDestination destination = DefaultDestination.builder()
    .exchangeName("tm.test.exchange")   //交换机名
    .queueName("tm.test.queue")         //队列名
    .routingKey("tm.test.key")          //路由键
    .exchangeType(ExchangeType.FANOUT)  //交换机类型
    .build();

DefaultEvent defaultEvent = DefaultEvent.builder()
    .businessKey(orderId)               //业务id
    .source("SAVE_ORDER")               //事件源（模块名）
    .sourceData(content)                //事件内容
    .build();

//事件发布
event.publish(destination, defaultEvent);
```



3、事件消息消费

+ a、订阅模式示例：

  ```java
  /**
   * 使用注解模式实现订阅消息
   *
   * rabbitmq如何做到消息不重复不丢失即使服务器重启
   *  1.exchange持久化（autoDelete属性默认为false，默认持久化）
   *  2.queue持久化（autoDelete属性默认为false，默认持久化）
   *  3.发送消息设置MessageDeliveryMode.persisent这个也是默认的行为
   *  4.手动确认
   *
   * @auther YangFan
   * @Date 2021/1/22 17:04
   */
  
  @Service
  public class RabbitMQService {
  
      /**
       * 1：当Queue中的 autoDelete 属性被设置为true时,
       *    那么，当消息接收着宕机，关闭后，消息队列则会删除.
       *    消息发送者一直发送消息，当消息接收者重新启动恢复正常后，会接收最新的消息，而宕机期间的消息则会丢失
       *
       * @param message
       * @param channel
       */
      @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "fanout_queue_email", autoDelete = "false"),//队列名自定义
                      exchange = @Exchange(value = "tm.test.exchange",//这个交换机名称要和发布时的交换机名称相同
                              type = ExchangeTypes.FANOUT))) //类型为发布订阅模式
      public void psubConsumerEmailAno(Message message, Channel channel) {
          try {
              /**
               * basicQos(prefetchSize, prefetchCount, prefetchCount)
               * prefetchSize：服务器传送最大内容量（以八位字节计算），如果没有限制，则为0
               * prefetchCount：会告诉RabbitMQ不要同时给一个消费者推送多于N个消息，即一旦有N个消息还没有ack，则该consumer将block掉，直到有消息ack
               * global：true\false 是否将上面设置应用于channel，简单点说，就是上面限制是channel级别的还是consumer级别
               *
               */
              channel.basicQos(0, 5, false);
              System.out.println("邮件业务接收到消息： "+ new String(message.getBody(), "UTF-8"));
  
              /**
               * 无异常就手动确认消息
               *    basicAck(long deliveryTag, boolean multiple)
               *    deliveryTag:取出来当前消息在队列中的的索引;
               *    multiple:为true的话就是批量确认,如果当前deliveryTag为5,那么就会确认
               *    deliveryTag为5及其以下的消息;一般设置为false
               */
              channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
          } catch (Exception e) {
  
              /**
               * 方案一：
               *      根据异常类型来选择是否重新放入队列
               *      basicNack(long deliveryTag, boolean multiple, boolean requeue)
               *      requeue:true为将消息重返当前消息队列,还可以重新发送给消费者;false:将消息丢弃
               *
               *      消息会不断发送，直到消息成功被消费，容易造成死循环
               *
               */
              /*try {
                  channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
              } catch (IOException e1) {
                  e1.printStackTrace();
              }*/
  
              /**
               * 方案二：
               *    先成功确认，然后通过channel.basicPublish()重新发布这个消息
               *
               */
  
              /**
               * 方案三：
               *    记录日志，记录消费失败的信息，数据定时调度，补偿消费
               *
               */
          }
      }
  }
  ```

  

+ b、此消费api的队列名要和生产的队列名保持一致，并且只能一次性消费

  代码示例：

  ```java
  /**
   * 事件消息消费，从mq中获取到订单信息
   */
  @Override
  public void subscribe() {
      String queueName = "tm.test.queue2";
      String exchangeName = "tm.test.exchange";
      try {
          String subscribe = EventBusHelper.subscribe(queueName, data -> {
              System.err.println("111========" + data);
              return true;
          });
          System.err.println("=============" + subscribe);
      } catch (Exception e) {
          throw new YErrorException("事件订阅出错!");
      }
  }
  ```

  

### sakura-flyway

> sql版本管理

加入依赖

```xml
<parent>
    <groupId>com.sakura</groupId>
    <artifactId>sakura-flyway</artifactId>
    <version>1.0</version>
</parent>
```

### sakura-file-util

> fastdfs文件上传下载工具类

1. 添加依赖

   ```xml
   <dependency>
       <groupId>com.sakura</groupId>
       <artifactId>sakura-file-util</artifactId>
       <version>1.0</version>
   </dependency>
   ```

2. 在springboot启动类上加

   ```java
   @Import(FdfsClientConfig.class)
   @EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
   ```

3. 配置yml文件

   ```yaml
   spring:  
     servlet:
       multipart:
         max-file-size: 100MB # 最大支持文件大小
         max-request-size: 100MB # 最大支持请求大小
   
   fdfs:
     # socket连接超时时长
     soTimeout: 1500
     # 连接tracker服务器超时时长
     connectTimeout: 600
     pool:
       # 从池中借出的对象的最大数目
       max-total: 153
       # 获取连接时的最大等待毫秒数100
       max-wait-millis: 102
     # 缩略图生成参数，可选
     thumbImage:
       width: 150
       height: 150
     # 跟踪服务器tracker_server请求地址,支持多个，这里只有一个，如果有多个在下方加- x.x.x.x:port
     trackerList:
       - 192.168.1.130:22122
     # 存储服务器storage_server访问地址
     web-server-url: http://192.168.1.130:8888/
   ```

4. 工具类 [FastDFSClient](https://github.com/yanjingfan/boot-parent/blob/master/sakura-file-util/src/main/java/com/sakura/common/fastdfs/FastDFSClient.java)

   + 上传文件对象

     ```java
     String str = FastDFSClient.uploadFile(file);
     ```

   + 上传缩略图

     ```java
     String str = FastDFSClient.uploadImageAndCrtThumbImage(MultipartFile multipartFile);
     ```

   + 下载文件

     ```java
     boolean downloadFile(String fileUrl, File file);
     ```

   + 删除文件

     ```java
     boolean deleteFile(String fileUrl);
     ```

5. 使用示例

   ```java
   @Test
   public void Upload() {
       String fileUrl = this.getClass().getResource("/fileUpload/redis-M-S.png").getPath();
       File file = new File(fileUrl);
       String str = FastDFSClient.uploadFile(file);
       FastDFSClient.getResAccessUrl(str);
   }
   
   @Test
   public void Delete() {
       //上传附件之后返回的url
       FastDFSClient.deleteFile("group1/M00/00/00/wKgjDGA4Zl2ARQNYAABZAfzrN84686.png");
   }
   ```

   