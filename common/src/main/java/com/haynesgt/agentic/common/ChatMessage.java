package com.haynesgt.agentic.common;

import lombok.Builder;
import lombok.Value;

@Builder
public record ChatMessage(String chatId, ChatMessageRole role, String text) {

}
