package com.haynesgt.agentic.server.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    private String id;

    private String chatId;

    private String text;
}
