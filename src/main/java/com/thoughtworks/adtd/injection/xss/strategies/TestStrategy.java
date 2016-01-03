package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.injection.xss.XssPayload;

/**
 * Interface for an XSS test strategy. Responsible for creating a request with an XSS payload.
 */
public interface TestStrategy {
    /**
     * Get the XSS payload being used in the strategy.
     * @return Payload.
     */
    XssPayload getXssPayload();

    /**
     * Create a request.
     * @param requestExecutor Request executor.
     * @return Request.
     * @throws Exception
     */
    Request createRequest(RequestExecutor requestExecutor) throws Exception;

    /**
     * Test whether a string matches the pattern for the strategy.
     * @param content Content to match against.
     * @return True if the pattern matches the content, false otherwise.
     */
    boolean matches(String content);
}
