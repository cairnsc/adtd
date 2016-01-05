package com.thoughtworks.adtd.http;

/**
 * Request executor.
 *
 * <p>When a request the executor is tied to is executed,
 *  1. {@link Request#execute(WebProxy)} is invoked by a caller.<br>
 *  1a. Request invokes {@link ResponseProcessor#prepare(Request)} for all its response processors.<br>
 *  1b. Request invokes {@link RequestExecutor#execute(WebProxy)}.<br>
 *  1c. Request verifies its conditions.<br>
 *  1d. Request invokes {@link ResponseProcessor#process(Request, Response)}  for all its response processors.<br>
 *  1e. Request invokes {@link RequestExecutor#process(Request, Response)}.<br>
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
