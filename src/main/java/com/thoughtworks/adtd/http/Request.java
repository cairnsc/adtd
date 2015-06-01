package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.util.MultiValueMap;

public interface Request {

    Request method(String value);
    String getMethod();
    Request uri(String value);
    String getUri();
    Request header(String name, String... values);
    MultiValueMap<String, String> getHeaders();
    Request param(String name, String... values);
    MultiValueMap<String, String> getParams();

    Response execute(WebProxy proxy) throws Exception;
    Response getResponse();

    /**
     * Set the status code expected in a response. Defaults to 200 (OK).
     * @param statusCode Status code.
     * @return This request.
     */
    Request expectStatusCode(int statusCode);

    /**
     * Get the status code expected in a response.
     * @return Status code.
     */
    int getExpectedStatusCode();

}
