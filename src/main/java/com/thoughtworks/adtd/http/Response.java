package com.thoughtworks.adtd.http;

/**
 * Response to a request.
 */
public interface Response {
    /**
     * Get the status code.
     * @return Status code.
     */
    int getStatus();

    /**
     * Get a header by name.
     * @param name Header name.
     * @return Header value, or null.
     */
    String getHeader(String name);

    /**
     * Get the body of the response.
     * @return Response body, or null if empty.
     * @throws Exception
     */
    String getBody() throws Exception;
}
