package com.haynesgt.agentic.worker;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;

import java.util.ArrayList;
import java.util.List;

public class AgentWorkflowImpl implements AgentWorkflow {

    private static final String INITIAL_MESSAGE = "Hello";

    private final ChatActivities chatActivities;

    private final List<ChatMessage> previousMessages = new ArrayList<>();
    private final List<ChatMessage> nextMessages = new ArrayList<>();
    // private final Map<String, String> agentNotes = new HashMap<>();

    public AgentWorkflowImpl(ChatActivities chatActivities) {
        this.chatActivities = chatActivities;

    }

    @Override
    public void chat(ChatInput chat) {
        // TODO continue-as-new for long chats
        if (previousMessages.isEmpty()) {
            sendMessage(chat.getChatId(), INITIAL_MESSAGE);
        }
        Workflow.await(this::hasNextMessage);

        previousMessages.addAll(nextMessages);
        nextMessages.clear();

        MyResponse response = generateResponse(previousMessages);
        Workflow.await(response.waitTime, this::hasNextMessage);
    }

    private MyResponse generateResponse(List<ChatMessage> previousMessages) {
        // run activity
        chatActivities.getResponse(new Chat(null));
        return new MyResponse();
    }

    @SignalMethod
    public void messageSignal(ChatMessage message) {
        this.nextMessages.add(message);
    }

    private void sendMessage(String chatId, String messageText) {
        ChatMessage message = ChatMessage.builder().role("agent").text(messageText).build();
        this.previousMessages.add(message);
        chatActivities.sendMessage(chatId, message);
    }

    private ChatMessage getNextMessage() {
        if (nextMessages.isEmpty()) {
            return null;
        }
        return nextMessages.remove(0);
    }

    private boolean hasNextMessage() {
        return !nextMessages.isEmpty();
    }
}
