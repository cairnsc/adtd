package com.thoughtworks.adtd.http;

import com.google.common.collect.Multimap;

import java.util.List;

public interface Request {
    Request method(String value);
    String getMethod();

    Request uri(String value);
    String getUri();

    /**
     * Add a header. If multiple values are provided, each is added as a new header. The order headers are added in is
     * preserved.
     * @param name Header name.
     * @param values One or more header values.
     * @return This request.
     */
    Request header(String name, String... values);

    /**
     * Set a header value at an index in the header list.
     * @param index Index.
     * @param value Header value.
     */
    void setHeader(int index, String value);

    /**
     * Get headers for a header name.
     * @param name Header name.
     * @return List of headers.
     */
    List<Header> getHeader(String name);

    /**
     * Get a multimap of request headers.
     * @return Multimap of headers.
     */
    Multimap<String, Header> getHeaders();

    /**
     * Add a request parameter. If multiple values are provided, it is treated as a multi-value parameter. The order
     * parameters are added in is preserved.
     * @param name Request parameter name.
     * @param values One or more request parameter values.
     * @return This request.
     */
    Request param(String name, String... values);

    /**
     * Get request parameters for a parameter name.
     * @param name Request parameter name.
     * @return List of request parameters.
     */
    List<RequestParameter> getParam(String name);

    /**
     * Get a request parameter at an index.
     * @param index Array index of request parameter.
     */
    RequestParameter getParam(int index);

    /**
     * Get the indices of request parameters for a parameter name.
     * @param name Request parameter name.
     * @return List of request parameters indexes.
     */
    List<Integer> paramIndexOf(String name);

    /**
     * Get request parameters.
     * @return Request parameters.
     */
    RequestParameters getParams();

    Request expect(ResponseCondition condition);
    Request expectIfUnset(ResponseCondition condition);
    List<ResponseCondition> getExpectations();

    Request processWith(ResponseProcessor processor);
    List<ResponseProcessor> getResponseProcessors();

    /**
     * Get context information about the request.
     * @return Context information about the request, or null if none is set.
     */
    String getContext();

    /**
     * Execute the request. This should never be invoked directly when created by a test, instead invoke the execute
     * method of the test.
     * @param proxy Web proxy to execute the request with.
     * @return Response.
     * @throws Exception
     */
    Response execute(WebProxy proxy) throws Exception;

    Response getResponse();
}
