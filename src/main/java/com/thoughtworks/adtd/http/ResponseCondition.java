package com.thoughtworks.adtd.http;

/**
 * Encapsulates a condition to be met in a response.
 */
public interface ResponseCondition {

    /**
     * Test whether a response matches an condition.
     * @param request The request the response is for.
     * @param response Response to test condition against.
     */
    void match(Request request, Response response) throws Exception;

}
