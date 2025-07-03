package com.haynesgt.agentic.server.repository;

import com.haynesgt.agentic.server.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatId(String chatId);
}
