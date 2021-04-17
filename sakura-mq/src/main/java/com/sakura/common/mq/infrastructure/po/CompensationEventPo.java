package com.sakura.common.mq.infrastructure.po;

import java.util.Date;

import lombok.Data;

/**
 * 补偿队列
 * 当尝试多次失败后，放入补偿队列，通过定时调度再次处理，如再次多次失败后等待人工处理
 */
@Data
public class CompensationEventPo {
    private String id;
    private String eventSourceId;
    private String error; //错误信息描述
    private String handler; //处理人
    private int state;//处理状态  0 待处理  1 已处理
    private String handlerResult; //人工处理结果说明
    private Date handlerDate;//处理时间
}
