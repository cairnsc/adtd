package com.thoughtworks.adtd.http;

/**
 * Request executor.
 *
 * When a request the executor is tied to is executed,
 *  1. Request.execute(WebProxy)
 *  1a. RequestExecutor.execute(WebProxy)
 *  1b. Request verifies its conditions
 *  1c. RequestExecutor.process(Request, Response)
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
     * Perform post-request execution processing if any is needed.
     * @param request Request.
     * @param response Response.
     * @throws Exception
     */
    void process(Request request, Response response) throws Exception;

}
