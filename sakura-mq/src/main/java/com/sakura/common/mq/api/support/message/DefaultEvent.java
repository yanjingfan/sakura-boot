package com.sakura.common.mq.api.support.message;

import lombok.Builder;

/**
 * @auther YangFan
 * @Date 2021/1/19 15:10
 */
@Builder
public class DefaultEvent implements DomainEvent {

    private String source;

    private String businessKey;

    private String sourceData;

    @Override
    public String source() {
        return source;
    }

    @Override
    public String businessKey() {
        return businessKey;
    }

    @Override
    public String sourceData() {
        return sourceData;
    }
}
