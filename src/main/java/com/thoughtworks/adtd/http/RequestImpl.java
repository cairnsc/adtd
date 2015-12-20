package com.thoughtworks.adtd.http;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RequestImpl implements Request {
    private RequestExecutor requestExecutor;
    private String method;
    private String uri;
    private final List<Header> headers;
    private final RequestParameters params;
    private final List<ResponseCondition> responseExpectations;
    private final List<ResponseProcessor> responseProcessors;
    private Response response;

    public RequestImpl(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
        headers = newArrayList();
        params = new RequestParameters();
        responseExpectations = newArrayList();
        responseProcessors = newArrayList();
    }

    public Request method(String value) {
        checkMutability();
        method = value;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Request uri(String value) {
        checkMutability();
        uri = value;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public Request header(String name, String... values) {
        checkMutability();
        for (String value : values) {
            headers.add(new Header(name, value));
        }
        return this;
    }

    public void setHeader(int index, String value) {
        checkMutability();
        headers.set(index, new Header(headers.get(index).getName(), value));
    }

    public List<Header> getHeader(String name) {
        List<Header> result = newArrayList();
        for (Header header : headers) {
            if (header.nameEquals(name)) {
                result.add(header);
            }
        }
        return result;
    }

    public Multimap<String, Header> getHeaders() {
        return Multimaps.index(headers, new Function<Header, String>() {
            public String apply(Header header) {
                return header.getName();
            }
        });
    }

    public Request param(String name, String... values) {
        checkMutability();
        params.param(name, values);
        return this;
    }

    public List<RequestParameter> getParam(String name) {
        return params.getParam(name);
    }

    public RequestParameter getParam(int index) {
        return params.getParam(index);
    }

    public List<Integer> paramIndexOf(String name) {
        return params.paramIndexOf(name);
    }

    public RequestParameters getParams() {
        return params;
    }

    public Request expect(ResponseCondition condition) {
        checkMutability();
        responseExpectations.add(condition);
        return this;
    }

    public Request expectIfUnset(ResponseCondition condition) {
        checkMutability();
        if (!hasResponseExpectationOfType(condition.getClass())) {
            responseExpectations.add(condition);
        }
        return this;
    }

    public List<ResponseCondition> getExpectations() {
        return Collections.unmodifiableList(responseExpectations);
    }

    public Request processWith(ResponseProcessor processor) {
        checkMutability();
        responseProcessors.add(processor);
        return this;
    }

    public List<ResponseProcessor> getResponseProcessors() {
        return Collections.unmodifiableList(responseProcessors);
    }

    public Response execute(WebProxy proxy) throws Exception {
        checkMutability();
        prepareResponseProcessors();

        response = requestExecutor.execute(proxy);
        if (response == null) {
            throw new InvalidResponseException("RequestExecutor execute returned null");
        }
        params.setImmutable();

        verifyConditions();
        processResponse();

        return response;
    }

    private void prepareResponseProcessors() {
        for (ResponseProcessor processor : responseProcessors) {
            processor.prepare(this);
        }
    }

    private void processResponse() throws Exception {
        for (ResponseProcessor processor : responseProcessors) {
            processor.process(this, response);
        }
        requestExecutor.process(this, response);
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

    private void checkMutability() {
        if (response != null) {
            throw new IllegalStateException("The test has already been executed");
        }
    }
}
