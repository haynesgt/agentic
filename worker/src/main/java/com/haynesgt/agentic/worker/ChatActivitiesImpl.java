package com.haynesgt.agentic.worker;

public class ChatActivitiesImpl implements ChatActivities {
    @Override
    public AgentReplyAndActions getAgentReplyAndActions(AgentRequest agentRequest) {
        return AgentReplyAndActions.builder().build();
    }

    @Override
    public void sendMessage(String chatId, ChatMessage message) {

    }
}
