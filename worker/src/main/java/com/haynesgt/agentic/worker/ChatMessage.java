package com.haynesgt.agentic.worker;

import lombok.Builder;

@Builder
public record ChatMessage(ChatMessageRole role, String text) {
}
