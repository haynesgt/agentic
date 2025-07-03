package com.haynesgt.agentic.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haynesgt.agentic.server.model.ChatMessage;
import com.haynesgt.agentic.server.repository.ChatMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class TelegramApiService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final ObjectMapper objectMapper;
    private final ChatMessageRepository chatMessageRepository;

    public TelegramApiService(ObjectMapper objectMapper, ChatMessageRepository chatMessageRepository) {
        this.objectMapper = objectMapper;
        this.chatMessageRepository = chatMessageRepository;
    }

    public void sendMessage(String chatId, String text) throws JsonProcessingException {
        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);

        // Simple JSON body
        SendMessageRequest body = new SendMessageRequest(chatId, text);
        new SendMessage()
        String jsonBody = objectMapper.writeValueAsString(body);

        chatMessageRepository.save(new ChatMessage(chatId, url, text));

        try (HttpClient client = HttpClient.newHttpClient()) {
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
