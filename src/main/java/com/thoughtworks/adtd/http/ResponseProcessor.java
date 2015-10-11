package com.thoughtworks.adtd.http;

public interface ResponseProcessor {
    /**
     * Process a response.
     * @param request Request.
     * @param response Response.
     * @throws Exception
     */
    void process(Request request, Response response) throws Exception;
}
