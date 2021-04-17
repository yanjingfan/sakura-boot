package com.sakura.common.mq.repository;


import com.sakura.common.mq.api.support.binding.Destination;
import com.sakura.common.mq.api.support.message.DomainEvent;

public interface EventRepository {
    boolean publish(Destination destination, DomainEvent domainEvent);

    String subscribe(String queueName);

    String subscribe2(String exchangeName, String queueName);
}
