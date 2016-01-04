package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;

/**
 * Test strategy for a request with a valid CSRF token. This is used to test the positive case.
 */
public class TestStrategyPositive implements TestStrategy {
    public static final String STRATEGY_NAME = "Valid (unmodified) CSRF token";
    private final int paramIndex;

    public TestStrategyPositive(int paramIndex) {
        this.paramIndex = paramIndex;
    }

    public String getStrategyName() {
        return STRATEGY_NAME;
    }

    public boolean isPositiveTest() {
        return true;
    }

    public int getParamIndex() {
        return paramIndex;
    }

    public void mutateRequest(Request request) {
        // do nothing for positive test
    }
}
