package com.haynesgt.agentic.worker;

import lombok.Builder;

import java.time.Duration;
import java.util.List;

@Builder
public record AgentReplyAndActions(
    String message,
    List<AgentTimer> addTimers,
    List<AgentTimer> removeTimers
) {
}
