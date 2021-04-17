package com.sakura.common.mq.job;

import com.sakura.common.mq.infrastructure.mq.RabbitMQManagement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 事件补偿调度
 */

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class EventCompensationJob {
    private static final Logger log = LoggerFactory.getLogger(EventCompensationJob.class);

    @Autowired
    private final RabbitMQManagement eventManagement;

    @Scheduled(fixedDelay = 10000)
    public void transactionalMessageCompensationTask() {
        long start = System.currentTimeMillis();
        log.debug("开始执行事务消息推送补偿定时任务...");
        eventManagement.processPendingCompensationRecords();
        long end = System.currentTimeMillis();
        log.debug("执行事务消息推送补偿定时任务完毕,耗时:{} ms...", end - start);
    }
}
