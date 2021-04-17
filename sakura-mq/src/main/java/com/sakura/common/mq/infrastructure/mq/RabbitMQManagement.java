package com.sakura.common.mq.infrastructure.mq;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sakura.common.mq.api.support.message.EventStatus;
import com.sakura.common.mq.infrastructure.dao.EventDataDao;
import com.sakura.common.mq.infrastructure.dao.EventRecordDao;
import com.sakura.common.mq.infrastructure.po.EventData;
import com.sakura.common.mq.infrastructure.po.EventRecord;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @auther YangFan
 * @Date 2021/1/27 17:19
 */

@Component
@RequiredArgsConstructor
public class RabbitMQManagement {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQManagement.class);

    @Autowired
    private final EventRecordDao eventRecordDao;

    @Autowired
    private final EventDataDao eventDataDao;

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    private static final LocalDateTime END = LocalDateTime.of(2999, 1, 1, 0, 0, 0);
    private static final long DEFAULT_INIT_BACKOFF = 10L;
    private static final int DEFAULT_BACKOFF_FACTOR = 2;
    private static final int DEFAULT_MAX_RETRY_TIMES = 5;
    private static final int LIMIT = 100;

    public void saveEventRecord(EventRecord record, String data) {
        record.setEventStatus(EventStatus.PENDING.getStatus());
        record.setNextScheduleTime(calculateNextScheduleTime(LocalDateTime.now(), DEFAULT_INIT_BACKOFF,
                DEFAULT_BACKOFF_FACTOR, 0));
        record.setCurrentRetryTimes(0);
        record.setInitBackoff(DEFAULT_INIT_BACKOFF);
        record.setBackoffFactor(DEFAULT_BACKOFF_FACTOR);
        record.setMaxRetryTimes(DEFAULT_MAX_RETRY_TIMES);
        eventRecordDao.insertSelective(record);
        EventData eventData = new EventData();
        eventData.setSourceData(data);
        eventData.setRecordId(record.getId());
        eventDataDao.insert(eventData);
    }

    public void sendEventSync(EventRecord record, String data) {
        try {
            rabbitTemplate.convertAndSend(record.getExchangeName(), record.getRoutingKey(), data);
            if (log.isDebugEnabled()) {
                log.debug("发送消息成功,目标队列:{},消息内容:{}", record.getQueueName(), data);
            }
            // 标记成功
            markSuccess(record);
        } catch (Exception e) {
            // 标记失败
            markFail(record, e);
        }
    }

    /**
     *
     * @param queueName
     * @return
     */
    public String receiveEvent(String queueName) {
        String message = "";
        try {
            message = (String)rabbitTemplate.receiveAndConvert(queueName);
        } catch (AmqpException e) {
            log.info("消费消息失败", e.getMessage());
        }
        return message;
    }

    /**
     * 消息订阅模式
     * @param exchangeName
     * @param queueName
     * @return
     */
    public String receiveEvents(String exchangeName, String queueName) {
        Connection connection = rabbitTemplate.getConnectionFactory().createConnection();
        Channel channel = connection.createChannel(true);


        /*ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("37621040");
        //设置 RabbitMQ 地址
        factory.setHost("192.168.35.12");
        //建立到代理服务器到连接
            Connection conn = factory.newConnection();
            //获得信道
            final Channel channel = conn.createChannel();
        **/
        try {

            //声明交换器
            channel.exchangeDeclare(exchangeName, "fanout", true);

            channel.queueDeclare(queueName, true, false, false, null);
            System.out.println("队列名称：" + queueName);

            //绑定队列，通过键 hola 将队列和交换器绑定起来
            channel.queueBind(queueName, exchangeName, "");

            //消费消息
            boolean autoAck = false;
            String consumerTag = "";
            channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    System.out.println("消费的路由键：" + routingKey);
                    System.out.println("消费的内容类型：" + contentType);
                    long deliveryTag = envelope.getDeliveryTag();
                    //确认消息
                    channel.basicAck(deliveryTag, false);
    //                    System.out.println(currentThreadName+"消费的消息体内容：");
                    String bodyStr = new String(body, "UTF-8");
                    System.out.println(bodyStr);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void markSuccess(EventRecord record) {
        // 标记下一次执行时间为最大值
        record.setNextScheduleTime(END);
        record.setCurrentRetryTimes(record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ?
                record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        record.setEventStatus(EventStatus.SUCCESS.getStatus());
        record.setEditTime(LocalDateTime.now());
        eventRecordDao.updateStatusSelective(record);
    }

    private void markFail(EventRecord record, Exception e) {
        log.error("发送消息失败,目标队列:{}", record.getQueueName(), e);
        record.setCurrentRetryTimes(record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ?
                record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        // 计算下一次的执行时间
        LocalDateTime nextScheduleTime = calculateNextScheduleTime(
                record.getNextScheduleTime(),
                record.getInitBackoff(),
                record.getBackoffFactor(),
                record.getCurrentRetryTimes()
        );
        record.setNextScheduleTime(nextScheduleTime);
        record.setEventStatus(EventStatus.FAIL.getStatus());
        record.setEditTime(LocalDateTime.now());
        eventRecordDao.updateStatusSelective(record);
    }

    /**
     * 指数退避算法，计算下一次执行时间
     *
     * @param base          基础时间
     * @param initBackoff   退避基准值
     * @param backoffFactor 退避指数
     * @param round         轮数
     * @return LocalDateTime
     */
    private LocalDateTime calculateNextScheduleTime(LocalDateTime base,
                                                    long initBackoff,
                                                    long backoffFactor,
                                                    long round) {
        double delta = initBackoff * Math.pow(backoffFactor, round);
        return base.plusSeconds((long) delta);
    }

    public void processPendingCompensationRecords() {
        // 这里预防把刚保存的消息也推送了，默认查询一小时前至十秒前的记录
        LocalDateTime max = LocalDateTime.now().plusSeconds(-DEFAULT_INIT_BACKOFF);
        LocalDateTime min = max.plusHours(-1);
        Map<Long, EventRecord> collect = eventRecordDao.queryPendingCompensationRecords(min, max, LIMIT)
                .stream()
                .collect(Collectors.toMap(EventRecord::getId, x -> x));
        if (!collect.isEmpty()) {
            StringJoiner joiner = new StringJoiner(",", "(", ")");
            collect.keySet().forEach(x -> joiner.add(x.toString()));
            eventDataDao.queryByEventIds(joiner.toString())
                    .forEach(item -> {
                        EventRecord record = collect.get(item.getRecordId());
                        sendEventSync(record, item.getSourceData());
                    });
        }
    }
}
