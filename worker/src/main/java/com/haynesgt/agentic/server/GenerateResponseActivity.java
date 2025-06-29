package com.haynesgt.agentic.server;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface GenerateResponseActivity {
    @ActivityMethod
    void getResponse(Chat chat);
}
