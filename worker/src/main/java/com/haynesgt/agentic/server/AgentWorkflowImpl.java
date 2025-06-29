package com.haynesgt.agentic.server;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.Workflow;

public class AgentWorkflowImpl implements AgentWorkflow {

    private MyMessage message;

    @Override
    public void chat(ChatInput chat) {
        // represents the flow of a chat with a user
        // control flow:
        // receive message
        // generate message and wait time
        // send message
        // wait or receive message, whichever is first
        Workflow.await(this::hasNextMessage);
        MyMessage message = getNextMessage();
        MyResponse response = generateResponse();
        Workflow.await(response.waitTime, this::hasNextMessage);
    }

    private MyResponse generateResponse() {
        // run activity
        return new MyResponse();
    }

    @SignalMethod
    public void messageSignal(MyMessage message) {
        this.message = message;
    }

    private MyMessage getNextMessage() {
        try {
            return message;
        } finally {
            message = null;
        }
    }

    private boolean hasNextMessage() {
        return message != null;
    }
}
