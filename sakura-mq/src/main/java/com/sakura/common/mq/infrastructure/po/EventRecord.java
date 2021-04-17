package com.sakura.common.mq.infrastructure.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * 发送的消息记录
 *
 * @auther YangFan
 * @Date 2021/1/19 10:37
 */
@Data
public class EventRecord {

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime editTime;

    private String creator;

    private String editor;

    //删除标志
    private Integer deleted;

    //当前重试次数
    private Integer currentRetryTimes;

    //最大重试次数
    private Integer maxRetryTimes;

    //队列名
    private String queueName;

    //交换器名
    private String exchangeName;

    //交换类型
    private String exchangeType;

    //路由键
    private String routingKey;

    //事件源，可理解为业务模块
    private String source;

    //业务键
    private String businessKey;

    //下一次调度时间
    private LocalDateTime nextScheduleTime;

    //事件状态，可理解为mq的消息状态
    private Integer eventStatus;

    //退避初始化值，用于设置时间
    private Long initBackoff;

    //退避因子(也就是指数)，用于设置时间
    private Integer backoffFactor;

}
