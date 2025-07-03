package com.haynesgt.agentic.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haynesgt.agentic.AgentChatService;
import com.haynesgt.agentic.server.dto.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("${telegram.bot.path}")
public class TelegramWebhookController {

    private static final Logger log = LoggerFactory.getLogger(TelegramWebhookController.class);

    @Value("${telegram.bot.token}")
    private String botToken;

    private final AgentChatService agentChatService;
    private ObjectMapper objectMapper;

    public TelegramWebhookController(AgentChatService agentChatService, ObjectMapper objectMapper) {
        this.agentChatService = agentChatService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) throws JsonProcessingException {
        log.info("Webhook received: {}", update);

        if (update.getMessage() != null && update.getMessage().getText() != null) {
            String chatId = update.getMessage().getChat().getId().toString();
            String replyText = agentChatService.getResponse(update.getMessage().getText());
            sendMessage(chatId, replyText);
        }

        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    private void sendMessage(String chatId, String text) throws JsonProcessingException {
        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);

        // Simple JSON body
        SendMessageRequest body = new SendMessageRequest(chatId, text);
        String jsonBody = objectMapper.writeValueAsString(body);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error("Failed to send message", e);
        }
    }
}
