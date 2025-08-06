package com.haynesgt.agentic.worker;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EntityScan(basePackages = {"com.haynesgt.agentic.common", "com.haynesgt.agentic.agent", "com.haynesgt.agentic.worker"})
@Configuration
public class AgenticTelegramWorkerApplication {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newServiceStubs(
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget("temporal:7233")
                        .build()
        );

        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker("MY_DEFAULT_TASK_QUEUE");

        worker.registerWorkflowImplementationTypes(AgentWorkflowImpl.class);

        worker.registerActivitiesImplementations(new ChatActivitiesImpl());

        factory.start();
    }
}
