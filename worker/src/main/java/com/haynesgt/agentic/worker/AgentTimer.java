package com.haynesgt.agentic.worker;

import lombok.Builder;

import java.time.Instant;

@Builder
record AgentTimer(String details, Instant time) {
}
