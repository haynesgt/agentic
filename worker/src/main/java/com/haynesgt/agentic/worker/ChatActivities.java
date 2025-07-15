package com.haynesgt.agentic.worker;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface ChatActivities {
    @ActivityMethod
    AgentReplyAndActions getAgentReplyAndActions(AgentRequest agentRequest);

    @ActivityMethod
    void sendMessage(String chatId, ChatMessage message);
}
