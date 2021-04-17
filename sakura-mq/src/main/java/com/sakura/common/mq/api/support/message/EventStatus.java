package com.sakura.common.mq.api.support.message;

/**
 * @auther YangFan
 * @Date 2021/1/19 15:08
 */
public enum EventStatus {
    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 待处理
     */
    PENDING(0),

    /**
     * 处理失败
     */
    FAIL(-1),

    ;

    private final Integer status;

    public Integer getStatus() {
        return status;
    }

    EventStatus(Integer status) {
        this.status = status;
    }
}
