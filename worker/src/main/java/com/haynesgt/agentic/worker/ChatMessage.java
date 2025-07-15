package com.haynesgt.agentic.worker;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ChatMessage {
    ChatMessageRole role;
    String text;
}
