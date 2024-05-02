package com.google.cloud.firestore.it;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ITTracingTestGlobalOtel extends ITTracingTest {
    @Override
    protected boolean isUsingGlobalOpenTelemetrySDK() {
        return true;
    }
}
