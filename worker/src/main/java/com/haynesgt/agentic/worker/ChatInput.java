package com.haynesgt.agentic.worker;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@JsonDeserialize
@Getter
public class ChatInput {
    String chatId;
}
