package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.injection.xss.XssPattern;

public interface TestStrategy {
    /**
     * Get the pattern being used in the strategy.
     * @return XSS pattern.
     */
    XssPattern getXssPattern();

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
