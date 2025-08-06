package com.haynesgt.agentic.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haynesgt.agentic.agent.AgentChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@RestController
public class TelegramWebhookController {

    private static final Logger log = LoggerFactory.getLogger(TelegramWebhookController.class);

    private final AgentChatService agentChatService;
    private final TelegramApiService telegramApiService;

    public TelegramWebhookController(AgentChatService agentChatService, TelegramApiService telegramApiService) {
        this.agentChatService = agentChatService;
        this.telegramApiService = telegramApiService;
    }

    @PostMapping("${telegram.bot.path}")
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) throws JsonProcessingException {
        log.info("Webhook received: {}", update);

        if (update.getMessage() != null && update.getMessage().getText() != null) {
            String chatId = update.getMessage().getChat().getId().toString();
            String replyText = agentChatService.getResponse(update.getMessage().getText());
            telegramApiService.sendMessage(chatId, replyText);
        }

        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
