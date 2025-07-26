package com.haynesgt.agentic.worker;

import com.haynesgt.agentic.common.AgentTimer;
import com.haynesgt.agentic.common.ChatMessage;
import lombok.Builder;

import java.util.List;

@Builder
public record AgentRequest(
    List<ChatMessage> messages,
    // Map<String, String> notes,
    List<AgentTimer> timers
) {
}
