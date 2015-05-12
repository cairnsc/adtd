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

    Response execute(WebProxy proxy);
}
