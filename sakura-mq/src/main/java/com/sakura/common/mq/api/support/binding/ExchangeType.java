package com.sakura.common.mq.api.support.binding;

/**
 * @auther YangFan
 * @Date 2021/1/19 15:21
 */
public enum ExchangeType {

    FANOUT("fanout"),

    DIRECT("direct"),

    TOPIC("topic"),

    DEFAULT(""),

    ;

    private final String type;

    ExchangeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
