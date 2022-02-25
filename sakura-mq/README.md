## 使用demo

### [sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)

## 

## 详细介绍

>  **注意**：引入此模块时，需要初始化数据库，脚本为[V2021010101__sakura-mq-1.0.0_初始化脚本.sql](https://github.com/yanjingfan/sakura-boot-demo/blob/master/web/src/main/resources/db/migration/V2021010101__sakura-mq-1.0.0_%E5%88%9D%E5%A7%8B%E5%8C%96%E8%84%9A%E6%9C%AC.sql)

+ 基于`rabbitmq`进行消息发布，发布失败的消息会入库，定时再次发布
