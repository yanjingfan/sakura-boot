package com.sakura.common.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @auther yangfan
 * @date 2022/2/24
 * @describle
 */
@Configuration
@EnableWebSocketMessageBroker  //注解开启STOMP协议来传输基于代理的消息，此时Controller层支持使用@MessageMapping
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 这是一个端点(endpoint)，客户端在订阅或发布消息到目的地路径前，要连接到该端点。
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/endpoint")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    /**
     * 配置消息代理(Message Broker)
     * @param config
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //基于内存的STOMP消息代理
        /**
         * 放开的前缀路由，放开topic和queue路由，客户端才能接收到对应路由开头的消息
         * topic用来广播，queue用来实现p2p
         */
        config.enableSimpleBroker("/topic", "/queue");

        /**
         * 客户端发消息到服务端，拥有@MessageMapping注解的方法路劲上 ，需要添加的前缀
         */
        config.setApplicationDestinationPrefixes("/app");

        //单播时，默认前缀为/user，现修改为/queue
        config.setUserDestinationPrefix("/queue");
    }

}
