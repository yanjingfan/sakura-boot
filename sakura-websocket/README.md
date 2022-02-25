## 使用demo

### [sakura-boot-demo](https://github.com/yanjingfan/sakura-boot-demo)

## 

## 详细介绍

+ 端点路由`/endpoint`

+ 使用`/topic`开头的路由，表示广播，一对多

+ 使用`/queue`开头的路由，表示单播，一对一

+ 当客户端发消息到服务端，在拥有`@MessageMapping`注解的方法路劲上 ，还需要添加的前缀`app`


