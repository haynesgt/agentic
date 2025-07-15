package com.haynesgt.agentic.worker;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record AgentRequest(
    List<ChatMessage> messages,
    // Map<String, String> notes,
    List<AgentTimer> timers
) {
}
