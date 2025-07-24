package com.haynesgt.agentic.server;


import com.haynesgt.agentic.server.model.ChatMessage;
import com.haynesgt.agentic.server.model.ChatResponse;
import com.haynesgt.agentic.server.repository.ChatMessageRepository;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.Workflow;
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

    @GetMapping
    public void sendMessage(@RequestParam String chatId, @RequestParam String message) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        client.newWorkflowStub(AgentWorkflow.class);
    }

    @GetMapping
    public void awaitNextMessage(@RequestParam String chatId) {

    }

    @GetMapping
    public ChatResponse getChat(@RequestParam String chatId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatId(chatId);
        return ChatResponse.builder()
                .messages(messages)
                .build();
    }
}
