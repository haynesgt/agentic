package com.haynesgt.agentic.common;

import lombok.Builder;

import java.time.Instant;

@Builder
public record AgentTimer(String details, Instant time) {
}
