package com.sakura.common.mq.api.support.binding;

/**
 * @auther YangFan
 * @Date 2021/1/19 15:21
 */
public interface Destination {

    ExchangeType exchangeType();

    String queueName();

    String exchangeName();

    String routingKey();
}
