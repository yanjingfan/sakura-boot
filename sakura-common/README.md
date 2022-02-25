## 使用demo

### [sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)

## 

## 详细介绍

+ 

+ 日志打印
  
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
