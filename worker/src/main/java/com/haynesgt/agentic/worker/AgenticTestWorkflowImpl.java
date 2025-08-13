package com.haynesgt.agentic.worker;

import com.haynesgt.agentic.common.AgenticTestWorkflow;

public class AgenticTestWorkflowImpl implements AgenticTestWorkflow {

    @Override
    public String test(String in) {
        return "tested: " + in;
    }
}
