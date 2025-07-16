package com.haynesgt.agentic.worker;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;

import java.util.List;

@JsonDeserialize
@Builder(toBuilder = true)
public record ChatInput(String chatId, List<ChatMessage> messages, List<AgentTimer> timers) {
}
