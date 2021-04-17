package com.sakura.common.mq.api.support.message;

/**
 *
 * 事件基础类型接口
 *
 * @auther YangFan
 * @Date 2021/1/19 15:10
 */
public interface DomainEvent {

    String source();

    String businessKey();

    String sourceData();
}
