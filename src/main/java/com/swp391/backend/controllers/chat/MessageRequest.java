package com.swp391.backend.controllers.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class MessageRequest {
    private Integer fromId;
    private String content;
    private Date sendTime;
    private Integer conversationId;
    private String chatterType;
}
