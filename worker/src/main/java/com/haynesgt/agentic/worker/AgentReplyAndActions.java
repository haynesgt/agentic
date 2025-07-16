package com.haynesgt.agentic.worker;

import lombok.Builder;

import java.util.List;

@Builder
public record AgentReplyAndActions(
    String messageText,
    List<AgentTimer> addTimers,
    List<AgentTimer> removeTimers
) {
}
