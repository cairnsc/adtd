package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.Response;

/**
 * Determines whether a {@link CsrfTokenTest} passed.
 */
public interface ResponseValidator {
    /**
     * Validate a response meets the expectations of a test.
     * @param test The test.
     * @param request Request.
     * @param response Response.
     * @return True if the response meets expectations, false otherwise.
     */
    boolean validate(CsrfTokenTest test, Request request, Response response);
}
