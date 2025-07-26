package com.haynesgt.agentic.server.repository;

import com.haynesgt.agentic.common.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByChatId(String chatId);
}
