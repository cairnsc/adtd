package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.util.AssertionFailureException;

/**
 * Interface for a CSRF test strategy. Responsible for mutating a CSRF token in a request for a test.
 */
public interface TestStrategy {
    /**
     * Get the name of the test strategy.
     * @return Test strategy name.
     */
    String getStrategyName();

    /**
     * Determine whether the test is positive (valid, non-breaking) or negative (invalid, breaking).
     * @return Boolean indicating whether the test is positive or negative.
     */
    boolean isPositiveTest();

    /**
     * Get the request parameter index of the parameter containing the CSRF token being tested.
     * @return Request parameter index.
     */
    int getParamIndex();

    /**
     * Mutate request
     * @param request Request.
     */
    void mutateRequest(Request request) throws AssertionFailureException;
}
