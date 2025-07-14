package com.haynesgt.agentic.worker;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface AgentWorkflow {
    // The Workflow Execution that starts this method can be initiated from code or
    // from the 'temporal' CLI utility.
    @WorkflowMethod
    void chat(ChatInput chat);
}
