package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.util.MultiValueMap;

import java.util.Collection;

public interface Request {

    Request method(String value);
    String getMethod();
    Request uri(String value);
    String getUri();
    Request header(String name, String... values);
    MultiValueMap<String, String> getHeaders();
    Request param(String name, String... values);
    MultiValueMap<String, String> getParams();

    Request expect(ResponseCondition condition);
    Request expectIfUnset(ResponseCondition condition);
    Collection<ResponseCondition> getExpectations();

    Response execute(WebProxy proxy) throws Exception;
    Response getResponse();

}
