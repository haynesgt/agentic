package com.haynesgt.agentic.server;

public class SendMessageRequest {
    private String chat_id;
    private String text;

    public SendMessageRequest(String chat_id, String text) {
        this.chat_id = chat_id;
        this.text = text;
    }

    public String getChat_id() {
        return chat_id;
    }

    public String getText() {
        return text;
    }
}