## 使用demo

### [sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)

## 

## 详细介绍

+ 数据库相关依赖，mysql连接依赖、MybatisPlus依赖、jpa依赖

+ MybatisPlus分页配置，扫描mapper文件的包路径为`com.sakura.cloud.**.mapper*`
  
  配置类：[MybatisPlusConfig.java](https://github.com/yanjingfan/boot-parent/blob/master/sakura-db/src/main/java/com/sakura/db/config/MybatisPlusConfig.java)

+ 封装简洁化MybatisPlus返回的分页对象
