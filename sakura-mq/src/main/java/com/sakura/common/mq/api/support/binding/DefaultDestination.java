package com.sakura.common.mq.api.support.binding;

import lombok.Builder;

/**
 * @auther YangFan
 * @Date 2021/1/19 15:21
 */
@Builder
public class DefaultDestination implements Destination {

    private ExchangeType exchangeType;

    private String queueName;

    private String exchangeName;

    private String routingKey;

    @Override
    public ExchangeType exchangeType() {
        return exchangeType;
    }

    @Override
    public String queueName() {
        return queueName;
    }

    @Override
    public String exchangeName() {
        return exchangeName;
    }

    @Override
    public String routingKey() {
        return routingKey;
    }
}
