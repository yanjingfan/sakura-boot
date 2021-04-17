package com.sakura.common.mq.api;

import com.sakura.common.mq.api.support.binding.Destination;
import com.sakura.common.mq.api.support.message.DomainEvent;
import com.sakura.common.mq.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 对外通过对象可发布订阅消息
 */
@Component
public class EventBus {

    @Autowired
    private EventRepository eventRepository;

    /**
     * 消息发布
     * @return
     */
    public boolean publish(Destination destination, DomainEvent domainEvent) {
        boolean publish = eventRepository.publish(destination, domainEvent);
        return publish;
    }

    /**
     * 消息订阅
     * @return
     */
    public String subscribe(String queueName) {
        return eventRepository.subscribe(queueName);
    }
    
}
