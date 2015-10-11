package com.thoughtworks.adtd.http;

import com.google.common.collect.Multimap;

import java.util.List;

public interface Request {
    Request method(String value);
    String getMethod();
    Request uri(String value);
    String getUri();
    Request header(String name, String... values);
    Multimap<String, String> getHeaders();
    Request param(String name, String... values);
    Multimap<String, String> getParams();

    Request expect(ResponseCondition condition);
    Request expectIfUnset(ResponseCondition condition);
    List<ResponseCondition> getExpectations();

    Request processWith(ResponseProcessor processor);
    List<ResponseProcessor> getResponseProcessors();

    Response execute(WebProxy proxy) throws Exception;

    Response getResponse();
}
