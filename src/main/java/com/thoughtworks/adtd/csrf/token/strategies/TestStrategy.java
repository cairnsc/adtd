package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.util.AssertionFailureException;

public interface TestStrategy {
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
