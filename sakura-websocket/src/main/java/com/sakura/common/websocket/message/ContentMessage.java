package com.sakura.common.websocket.message;

import lombok.Data;

@Data
public class ContentMessage {

    private String sendUrl;

    private String content;
}
