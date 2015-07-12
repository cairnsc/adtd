package com.thoughtworks.adtd.http;

public interface ResponseValidator {

    /**
     * Validate a response meets the expectations of a test.
     * @param request
     * @param response
     * @return True if the response meets expectations, false otherwise.
     */
    boolean validate(Request request, Response response);

}
