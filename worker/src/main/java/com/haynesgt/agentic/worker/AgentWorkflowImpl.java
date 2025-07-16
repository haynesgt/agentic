package com.haynesgt.agentic.worker;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AgentWorkflowImpl implements AgentWorkflow {

    private static final String INITIAL_MESSAGE = "Hello";

    private final ChatActivities chatActivities;

    private final List<ChatMessage> nextMessages = new ArrayList<>();

    public AgentWorkflowImpl(ChatActivities chatActivities) {
        this.chatActivities = chatActivities;

    }

    @Override
    public void chat(ChatInput chat) {
        Instant now = Instant.ofEpochMilli(Workflow.currentTimeMillis());
        List<ChatMessage> previousMessages = chat.messages();
        String messageText;
        List<AgentTimer> newTimers;
        if (previousMessages.isEmpty()) {
            messageText = INITIAL_MESSAGE;
            newTimers = chat.timers();
        } else {
            AgentReplyAndActions response = getAgentReplyAndActions(chat);
            messageText = response.messageText();
            newTimers = Stream.concat(chat.timers().stream(), response.addTimers().stream())
                    .filter(timer -> timer.time() != null)
                    .filter(timer -> timer.time().isBefore(now))
                    .filter(timer -> !response.removeTimers().contains(timer))
                    .collect(Collectors.toList());
        }

        sendMessage(chat.chatId(), messageText);

        Optional<AgentTimer> nextTimer = newTimers.stream().min(Comparator.comparing(AgentTimer::time));
        Duration durationToNextTimer = nextTimer.map(AgentTimer::time)
                .map(nextTimerTime -> Duration.between(Instant.ofEpochMilli(Workflow.currentTimeMillis()), nextTimerTime))
                .orElse(null);
        Workflow.await(durationToNextTimer, this::hasNextMessage);

        Workflow.continueAsNew(
            chat.toBuilder()
                .messages(
                    Stream.concat(
                        Stream.concat(
                            previousMessages.stream(),
                            nextMessages.stream()
                        ),
                        Stream.of(ChatMessage.builder().text(messageText).build())
                    ).collect(Collectors.toList())
                )
                .timers(newTimers)
        );
    }

    private AgentReplyAndActions getAgentReplyAndActions(ChatInput chat) {
        // history -> messageText + actions
        return chatActivities.getAgentReplyAndActions(
                AgentRequest.builder()
                        .messages(chat.messages())
                        // .notes(notes)
                        .timers(chat.timers())
                        .build()
        );
    }

    @SignalMethod
    public void messageSignal(ChatMessage message) {
        this.nextMessages.add(message);
    }

    @UpdateMethod
    public void messageUpdate(ChatMessage message) {
        this.nextMessages.add(message);
    }

    private void sendMessage(String chatId, String messageText) {
        ChatMessage message = ChatMessage.builder().role(ChatMessageRole.AGENT).text(messageText).build();
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
