package com.thoughtworks.adtd.http;

/**
 * Request executor.
 *
 * When a request the executor is tied to is executed,
 *  1. Request.execute(WebProxy) is invoked by a caller
 *  1a. Request invokes prepare for all its response processors
 *  1b. Request invokes RequestExecutor.execute(WebProxy)
 *  1c. Request verifies its conditions
 *  1d. Request invokes all its response processors
 *  1e. Request invokes RequestExecutor.prepare(Request, Response)
 */
public interface RequestExecutor {
    /**
     * Execute the request against a web proxy. The executor has a final opportunity to manipulate the request before
     * executing it.
     * @param proxy Web proxy to execute the request again.
     * @return Response
     * @throws Exception
     */
    Response execute(WebProxy proxy) throws Exception;

    /**
     * Process a response.
     * @param request Request.
     * @param response Response.
     * @throws Exception
     */
    void process(Request request, Response response) throws Exception;
}
