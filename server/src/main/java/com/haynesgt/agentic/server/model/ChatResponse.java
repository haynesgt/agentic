package com.haynesgt.agentic.server.model;

import com.haynesgt.agentic.common.ChatMessageEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record ChatResponse(
        List<ChatMessageEntity> messages
) {
}
