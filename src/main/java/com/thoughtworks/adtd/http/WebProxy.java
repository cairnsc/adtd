package com.thoughtworks.adtd.http;

/**
 * Web proxy. An adapter responsible for executing a request using the web framework and test framework of choice.
 */
public interface WebProxy {
    /**
     * Execute a request.
     * @param request Request.
     * @return Response to the request.
     * @throws Exception
     */
    Response execute(Request request) throws Exception;
}
