package com.haynesgt.agentic.server;

import com.haynesgt.agentic.AgentChatService;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AgentChatService.class)
@EntityScan(basePackages = {"com.haynesgt.agentic.common","com.haynesgt.agentic.server"})
@Configuration
public class AgenticServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgenticServerApplication.class, args);
    }

    @Bean
    public WorkflowClient getWorkflowClient() {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        return WorkflowClient.newInstance(service);
    }
}
