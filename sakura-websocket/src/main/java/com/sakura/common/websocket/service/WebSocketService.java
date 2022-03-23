package com.sakura.common.websocket.service;

import com.sakura.common.websocket.config.WebSocketConfig;
import com.sakura.common.websocket.message.ContentMessage;
import com.sakura.common.websocket.message.InMessage;
import com.sakura.common.websocket.message.OutMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @auther yangfan
 * @date 2022/2/25
 * @describle 封装了websocket工具类
 */
@Slf4j
@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * 单播
     * @param message
     */
    public void queue(InMessage message) {
        /**
         * 发送路径除了固定的路径 还拼接了特定的接收用户的标识（一般为用户id）
         *
         * 默认前缀为/user + message.getTo() + /message，可以在{@link WebSocketConfig}
         * 中的configureMessageBroker方法修改默认前缀，这里改成了/queue的前缀
         */
        log.info("点对点发送消息===============" + message.getTo());
        template.convertAndSendToUser(message.getTo(),"/message", new OutMessage<>(message.getMsgType(), message.getContent()));
    }

    /**
     * 广播
     */
    public void subscribe(InMessage message) {
        log.info("路由与内容===============" + message.getTo());
        template.convertAndSend("/topic/" + message.getTo() + "/message", new OutMessage<>(message.getContent()));
    }

    /**
     * 路由与内容
     */
    public void sendMessage(ContentMessage contentMessage) {
        log.info("路由与内容===============" + contentMessage);
        template.convertAndSend(contentMessage.getSendUrl(), contentMessage.getContent());
    }

}
