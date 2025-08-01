package com.haynesgt.agentic.server;

import com.haynesgt.agentic.AgentChatService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AgentChatService.class)
@EntityScan(basePackages = "com.haynesgt.agentic.common")
public class AgenticServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgenticServerApplication.class, args);
    }
}
