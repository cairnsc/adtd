package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.util.MultiValueLinkedHashMap;
import com.thoughtworks.adtd.util.MultiValueMap;

public class RequestImpl implements Request {

    private String method;
    private String uri;
    private final MultiValueMap<String, String> headers;
    private final MultiValueMap<String, String> params;
    private RequestSubject executor;

    public RequestImpl(RequestSubject executor) {
        this.executor = executor;
        headers = new MultiValueLinkedHashMap<String, String>();
        params = new MultiValueLinkedHashMap<String, String>();
    }

    public Request method(String value) {
        method = value;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Request uri(String value) {
        uri = value;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public Request header(String name, String... values) {
        headers.add(name, values);
        return this;
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public Request param(String name, String... values) {
        params.add(name, values);
        return this;
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    public Response execute(WebProxy proxy) {
        return null;
    }

}
