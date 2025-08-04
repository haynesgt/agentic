package com.haynesgt.agentic.server;


import com.haynesgt.agentic.common.AgentWorkflow;
import com.haynesgt.agentic.common.ChatInput;
import com.haynesgt.agentic.common.ChatMessage;
import com.haynesgt.agentic.common.ChatMessageEntity;
import com.haynesgt.agentic.server.model.ChatResponse;
import com.haynesgt.agentic.server.repository.ChatMessageRepository;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SimpleAgentController {

    ChatMessageRepository chatMessageRepository;

    WorkflowClient client;

    @Autowired
    public SimpleAgentController(ChatMessageRepository chatMessageRepository,  WorkflowClient client) {
        this.chatMessageRepository = chatMessageRepository;
        this.client = client;
    }

    @GetMapping("/send-message")
    public void sendMessage(@RequestParam String chatId, @RequestParam String message) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        AgentWorkflow agentWorkflow = client.newWorkflowStub(AgentWorkflow.class, "chat-%1$s".formatted(chatId));
        agentWorkflow.chat(ChatInput.builder()
                .chatId(chatId)
                        .messages(List.of(ChatMessage.builder().text(message).build()))
                .build());
    }

    @GetMapping("await-next-message")
    public void awaitNextMessage(@RequestParam String chatId) {

    }

    @GetMapping("get-chat")
    public ChatResponse getChat(@RequestParam String chatId) {
        List<ChatMessageEntity> messages = chatMessageRepository.findByChatId(chatId);
        return ChatResponse.builder()
                .messages(messages)
                .build();
    }
}
