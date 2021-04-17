package com.sakura.common.mq.infrastructure.po;

import lombok.Data;

import java.util.Date;

/**
 * 事件源
 */
@Data
public class EventSourcePo {

    private Long id;

    //事务消息记录ID
    private Long recordId;

    //事件内容
    private String sourceData;
}
