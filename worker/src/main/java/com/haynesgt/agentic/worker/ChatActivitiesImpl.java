package com.haynesgt.agentic.worker;

import com.haynesgt.agentic.common.ChatMessage;

public class ChatActivitiesImpl implements ChatActivities {
    @Override
    public AgentReplyAndActions getAgentReplyAndActions(AgentRequest agentRequest) {
        return AgentReplyAndActions.builder()
                .messageText("sample text")
                .build();
    }

    @Override
    public void sendMessage(String chatId, ChatMessage message) {
        //
    }
}
