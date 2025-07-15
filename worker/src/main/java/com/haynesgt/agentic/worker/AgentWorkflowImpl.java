package com.haynesgt.agentic.worker;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AgentWorkflowImpl implements AgentWorkflow {

    private static final String INITIAL_MESSAGE = "Hello";

    private final ChatActivities chatActivities;

    private final List<ChatMessage> previousMessages = new ArrayList<>();
    private final List<ChatMessage> nextMessages = new ArrayList<>();
    private final Map<String, String> notes = new HashMap<>();
    private final List<AgentTimer> timers = new ArrayList<>();

    public AgentWorkflowImpl(ChatActivities chatActivities) {
        this.chatActivities = chatActivities;

    }

    @Override
    public void chat(ChatInput chat) {
        // TODO continue-as-new for long chats
        if (previousMessages.isEmpty()) {
            sendMessage(chat.getChatId(), INITIAL_MESSAGE);
        }

        Duration durationToNextTimer = timers.isEmpty() ? null : Duration.between(Instant.ofEpochMilli(Workflow.currentTimeMillis()), timers.getFirst().time());
        Workflow.await(durationToNextTimer, this::hasNextMessage);

        //noinspection InfiniteLoopStatement
        while (true) {
            previousMessages.addAll(nextMessages);
            nextMessages.clear();

            AgentReplyAndActions response = getAgentReplyAndActions(previousMessages);
            timers.addAll(response.addTimers());
            timers.removeIf(
                    timer -> timer.time() == null || timer.time().isAfter(Instant.ofEpochMilli(Workflow.currentTimeMillis())) || response.removeTimers().contains(timer)
            );
            timers.sort(Comparator.comparing(AgentTimer::time));

            // find time to next message

            Workflow.await(null, this::hasNextMessage);
            Workflow.continueAsNew(
                    chat
            );
        }
    }

    private AgentReplyAndActions getAgentReplyAndActions(List<ChatMessage> previousMessages) {
        // history -> message + actions
        chatActivities.getAgentReplyAndActions(
                AgentRequest.builder()
                        .messages(previousMessages)
                        // .notes(notes)
                        .timers(timers)
                        .build()
        );
        return new AgentReplyAndActions();
    }

    @SignalMethod
    public void messageSignal(ChatMessage message) {
        this.nextMessages.add(message);
    }

    private void sendMessage(String chatId, String messageText) {
        ChatMessage message = ChatMessage.builder().role(ChatMessageRole.AGENT).text(messageText).build();
        this.previousMessages.add(message);
        chatActivities.sendMessage(chatId, message);
    }

    private ChatMessage getNextMessage() {
        if (nextMessages.isEmpty()) {
            return null;
        }
        return nextMessages.removeFirst();
    }

    private boolean hasNextMessage() {
        return !nextMessages.isEmpty();
    }
}
