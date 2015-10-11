package com.thoughtworks.adtd.http;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class RequestImpl implements Request {
    private RequestExecutor executor;
    private String method;
    private String uri;
    private final LinkedListMultimap<String, String> headers;
    private final LinkedListMultimap<String, String> params;
    private final List<ResponseCondition> responseExpectations;
    private final List<ResponseProcessor> responseProcessors;
    private Response response;

    public RequestImpl(RequestExecutor executor) {
        this.executor = executor;
        headers = LinkedListMultimap.create();
        params = LinkedListMultimap.create();
        responseExpectations = newArrayList();
        responseProcessors = newArrayList();
        processWith(executor);
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
            headers.put(name, value);
        }
        return this;
    }

    public Multimap<String, String> getHeaders() {
        return Multimaps.unmodifiableMultimap(headers);
    }

    public Request param(String name, String... values) {
        checkMutability();
        for (String value : values) {
            params.put(name, value);
        }
        return this;
    }

    public Multimap<String, String> getParams() {
        return Multimaps.unmodifiableMultimap(params);
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

        response = executor.execute(proxy);
        if (response == null) {
            throw new InvalidResponseException("RequestExecutor execute returned null");
        }

        verifyConditions();
        processResponse();

        return response;
    }

    private void processResponse() throws Exception {
        for (ResponseProcessor processor : responseProcessors) {
            processor.process(this, response);
        }
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
