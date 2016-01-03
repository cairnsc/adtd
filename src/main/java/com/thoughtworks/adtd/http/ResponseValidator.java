package com.thoughtworks.adtd.http;

/**
 * Determines whether or not a test passed.
 */
public interface ResponseValidator {
    /**
     * Validate a response meets the expectations of a test.
     * @param request Request.
     * @param response Response.
     * @return True if the response meets expectations, false otherwise.
     */
    boolean validate(Request request, Response response);
}
