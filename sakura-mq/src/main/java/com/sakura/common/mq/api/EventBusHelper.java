package com.sakura.common.mq.api;

import com.sakura.common.mq.api.support.binding.Destination;
import com.sakura.common.mq.api.support.message.DomainEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 对外可通过静态方法进行发布订阅
 */
@Component
public class EventBusHelper {

    private EventBusHelper(EventBus eb){
        eventBus = eb;
    }

    private static EventBus eventBus;
    private static EventBusHelper eventBusHelper;

    @Resource
    private EventBus eb;

    @PostConstruct
    private void init() {
        eventBusHelper = new EventBusHelper(eb);
        eventBus = eb;
    }
    
    /**
     * 消息发布
     * @return
     */
    public static boolean publish(Destination destination, DomainEvent domainEvent) {
        boolean publish = eventBus.publish(destination, domainEvent);
        return publish;
    }

    /**
     * 消息订阅
     * @return
     */
    public static String subscribe(String queueName, EventHandler handler) {
        String data = eventBus.subscribe(queueName);
        handler.handler(data);
        return data;
    }
}
