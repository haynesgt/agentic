package com.haynesgt.agentic.server.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatResponse(
        List<ChatMessage> messages
) {
}
