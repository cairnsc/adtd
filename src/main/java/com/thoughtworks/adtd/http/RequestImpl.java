package com.thoughtworks.adtd.http;

import com.thoughtworks.adtd.util.MultiValueLinkedHashMap;
import com.thoughtworks.adtd.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RequestImpl implements Request {

    private RequestExecutor executor;
    private String method;
    private String uri;
    private final MultiValueMap<String, String> headers;
    private final MultiValueMap<String, String> params;
    private final List<ResponseCondition> responseExpectations;
    private Response response;

    public RequestImpl(RequestExecutor executor) {
        this.executor = executor;
        headers = new MultiValueLinkedHashMap<String, String>();
        params = new MultiValueLinkedHashMap<String, String>();
        responseExpectations = new ArrayList<ResponseCondition>();
    }

    public Request method(String value) {
        if (response != null) {
            throw new IllegalStateException("The test has already been executed");
        }

        method = value;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Request uri(String value) {
        if (response != null) {
            throw new IllegalStateException("The test has already been executed");
        }

        uri = value;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public Request header(String name, String... values) {
        if (response != null) {
            throw new IllegalStateException("The test has already been executed");
        }

        headers.add(name, values);
        return this;
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public Request param(String name, String... values) {
        if (response != null) {
            throw new IllegalStateException("The test has already been executed");
        }

        params.add(name, values);
        return this;
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    public Request expect(ResponseCondition condition) {
        if (response != null) {
            throw new IllegalStateException("The test has already been executed");
        }

        responseExpectations.add(condition);
        return this;
    }

    public Request expectIfUnset(ResponseCondition condition) {
        if (response != null) {
            throw new IllegalStateException("The test has already been executed");
        }

        if (!hasResponseExpectationOfType(condition.getClass())) {
            responseExpectations.add(condition);
        }
        return this;
    }

    public Collection<ResponseCondition> getExpectations() {
        // REVISIT: return an immutable collection
        return responseExpectations;
    }

    public Response execute(WebProxy proxy) throws Exception {
        if (response != null) {
            throw new IllegalStateException("The test has already been executed");
        }

        response = executor.execute(proxy);
        if (response == null) {
            throw new InvalidResponseException("RequestExecutor execute returned null");
        }

        verifyConditions();

        return response;
    }

    public Response getResponse() {
        return response;
    }

    private boolean hasResponseExpectationOfType(Class<? extends ResponseCondition> clazz) {
        for (ResponseCondition condition : responseExpectations) {
            if (condition.getClass() == clazz) {
                return true;
            }
        }
        return false;
    }

    private void verifyConditions() throws Exception {
        for (ResponseCondition condition : responseExpectations) {
            condition.match(this, response);
        }
    }

}
