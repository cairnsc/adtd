package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;

public class TestStrategyPositive implements TestStrategy {
    private final int paramIndex;

    public TestStrategyPositive(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public void mutateRequest(Request request) {
        // do nothing for positive test
    }
}
