package com.haynesgt.agentic.server.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {
    private Long messageId;
    private Chat chat;
    private String text;
}
