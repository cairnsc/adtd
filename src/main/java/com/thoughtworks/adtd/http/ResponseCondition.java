package com.thoughtworks.adtd.http;

/**
 * Represents a condition that must be met in a response.
 */
public interface ResponseCondition {
    /**
     * Test whether a response matches an condition.
     * @param request Request.
     * @param response Response to test condition against.
     */
    void match(Request request, Response response) throws Exception;
}
