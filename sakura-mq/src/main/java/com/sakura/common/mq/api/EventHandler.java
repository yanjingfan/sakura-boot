package com.sakura.common.mq.api;

@FunctionalInterface
public interface EventHandler {
    
    //消费者处理业务逻辑方法
    boolean handler(String data);
}
