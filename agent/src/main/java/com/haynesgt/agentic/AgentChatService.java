package com.haynesgt.agentic;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@Service
public class AgentChatService {
    private static final Logger log = LoggerFactory.getLogger(AgentChatService.class);

    interface MyAssistant {

        // can use @SystemPrompt
        String getResponse(String message);
    }

    ChatModel chatModel;
    MyAssistant assistant;

    AgentChatService() {
        chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(GPT_4_O_MINI)
                .build();

        assistant = AiServices.create(MyAssistant.class, chatModel);
    }

    public static void main(String[] args) {
        String answer = new AgentChatService().getResponse("Hello.");
        System.out.println(answer); // Hello! How can I assist you today?
    }

    public String getResponse(String input) {
        log.info(input);
        String response = assistant.getResponse(input);
        log.info(response);
        return response;
    }
}
