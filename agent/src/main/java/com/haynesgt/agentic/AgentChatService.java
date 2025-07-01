package com.haynesgt.agentic;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import org.springframework.stereotype.Service;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@Service
public class AgentChatService {
    interface DogLovingAssistant {

        @SystemMessage("Quick response")
        String getJokes(String message);
    }

    ChatModel chatModel;
    DogLovingAssistant assistant;

    AgentChatService() {
        chatModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(GPT_4_O_MINI)
                .build();

        assistant = AiServices.create(DogLovingAssistant.class, chatModel);
    }

    public static void main(String[] args) {
        String answer = new AgentChatService().getResponse("Hello.");
        System.out.println(answer); // Hello! How can I assist you today?
    }

    public String getResponse(String input) {
        return assistant.getJokes(input);
    }
}
