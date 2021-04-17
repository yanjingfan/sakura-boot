package com.sakura.common.mq.repository.impl;



import com.sakura.common.mq.infrastructure.mq.RabbitMQManagement;
import com.sakura.common.mq.infrastructure.po.EventRecord;
import com.sakura.common.mq.repository.EventRepository;
import com.sakura.common.mq.api.support.binding.Destination;
import com.sakura.common.mq.api.support.binding.ExchangeType;
import com.sakura.common.mq.api.support.message.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 封装底层, 其中EventManagement可以通过配置MQ类型工厂创建
 */

@Service
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    @Autowired
    private final AmqpAdmin amqpAdmin;

    @Autowired
    private final RabbitMQManagement eventManagement;

    private static final ConcurrentMap<String, Boolean> QUEUE_ALREADY_DECLARE = new ConcurrentHashMap<>();

    @Override
    public boolean publish(Destination destination, DomainEvent domainEvent) {
        String queueName = destination.queueName();
        String exchangeName = destination.exchangeName();
        String routingKey = destination.routingKey();
        ExchangeType exchangeType = destination.exchangeType();
        // 原子性的预声明
        QUEUE_ALREADY_DECLARE.computeIfAbsent(queueName, k -> {
            Queue queue = new Queue(queueName);
            amqpAdmin.declareQueue(queue);
            Exchange exchange = new CustomExchange(exchangeName, exchangeType.getType());
            amqpAdmin.declareExchange(exchange);
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
            amqpAdmin.declareBinding(binding);
            return true;
        });
        EventRecord record = new EventRecord();
        record.setQueueName(queueName);
        record.setExchangeName(exchangeName);
        record.setExchangeType(exchangeType.getType());
        record.setRoutingKey(routingKey);
        record.setSource(domainEvent.source());
        record.setBusinessKey(domainEvent.businessKey());
        String data = domainEvent.sourceData();
        // 保存事务消息记录
        eventManagement.saveEventRecord(record, data);
        // 注册事务同步器
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                eventManagement.sendEventSync(record, data);
            }
        });
        return true;
    }

    @Override
    public String subscribe(String queueName) {
        return eventManagement.receiveEvent(queueName);
    }

    @Override
    public String subscribe2(String exchangeName, String queueName) {
        return eventManagement.receiveEvents(exchangeName, queueName);
    }
    
}
