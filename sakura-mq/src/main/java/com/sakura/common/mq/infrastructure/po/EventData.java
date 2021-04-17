package com.sakura.common.mq.infrastructure.po;

import lombok.Data;

/**
 *
 * 发送的消息内容
 *
 * @auther YangFan
 * @Date 2021/1/19 10:43
 */
@Data
public class EventData {

    private Long id;

    //事务消息记录ID
    private Long recordId;

    //事件内容
    private String sourceData;

}
