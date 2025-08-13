package com.haynesgt.agentic.common;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface AgenticTestWorkflow {
    @WorkflowMethod
    String test(String in);
}
