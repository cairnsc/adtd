package com.thoughtworks.adtd.http;

public interface ResponseProcessor {
    /**
     * Prepare a request immediately prior to execution.
     * @param request Request.
     */
    void prepare(Request request);

    /**
     * Process a response.
     * @param request Request.
     * @param response Response.
     * @throws Exception
     */
    void process(Request request, Response response) throws Exception;
}
