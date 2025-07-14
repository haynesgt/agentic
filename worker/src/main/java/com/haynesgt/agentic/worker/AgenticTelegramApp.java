package com.haynesgt.agentic.worker;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class AgenticTelegramApp {

    public static void main(String[] args) throws Exception {

        // In the Java SDK, a stub represents an element that participates in
        // Temporal orchestration and communicates using gRPC.

        // A WorkflowServiceStubs communicates with the Temporal front-end service.
        WorkflowServiceStubs serviceStub = WorkflowServiceStubs.newLocalServiceStubs();

        // A WorkflowClient wraps the stub.
        // It can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(serviceStub);

        // Workflow options configure  Workflow stubs.
        // A WorkflowId prevents duplicate instances, which are removed.
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(Shared.AGENT_MAIN_TASK_QUEUE)
                .setWorkflowId("money-transfer-workflow")
                .build();

        System.exit(0);
    }
}
