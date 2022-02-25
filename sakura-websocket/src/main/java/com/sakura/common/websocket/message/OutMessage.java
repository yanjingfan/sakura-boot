package com.sakura.common.websocket.message;

import lombok.Data;

import java.util.Date;

/**
 * 发送的消息
 * from：前端拿到发送的可以做一些事
 * 没有to字段 因为已经发送到接收者 所以没有意义
 */
@Data
public class OutMessage<T> {

    /**
     * 消息类型
     */
    private Integer msgType;

    private String from;

    private T content;

    private Date time = new Date();

    public OutMessage(T content) {
        this.content = content;
    }

    public OutMessage(Integer msgType, T content) {
        this.msgType = msgType;
        this.content = content;
    }
}
