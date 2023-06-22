package com.swp391.backend.controllers.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ConversationRequest {
    private Integer fromId;
    private Integer toId;
    private String content;
    private Date sendTime;
    private String chatterType;
}
