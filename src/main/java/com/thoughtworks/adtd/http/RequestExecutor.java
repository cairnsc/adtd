package com.thoughtworks.adtd.http;

/**
 * Request executor.
 *
 * When a request the executor is tied to is executed,
 *  1. Request.execute(WebProxy)
 *  1a. RequestExecutor.execute(WebProxy)
 *  1b. Request verifies its conditions
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
}
