## 使用demo

### [sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)



## 单机动态定时

**注意**：使用此模块时，需要先初始化sql脚本：[V2021101801__sakura-cron-1.0.0_动态定时任务.sql](https://github.com/yanjingfan/sakura-boot-demo/blob/master/web/src/main/resources/db/migration/V2021101801__sakura-cron-1.0.0_%E5%8A%A8%E6%80%81%E5%AE%9A%E6%97%B6%E4%BB%BB%E5%8A%A1.sql)

+ 单机动态定时器配置
  
  可以动态开启或者关闭定时任务，动态修改定时任务的cron表达式等等

+ 分布式定时任务
  
  可自行放开`PowerJob`的依赖（这里注释掉了），使用`PowerJob`分布式调度时，先看看其官网使用文档：[PowerJob · 语雀 ](https://www.yuque.com/powerjob/guidence/intro)
