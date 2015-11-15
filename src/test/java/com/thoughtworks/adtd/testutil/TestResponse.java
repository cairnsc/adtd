package com.thoughtworks.adtd.testutil;

import com.google.common.collect.LinkedListMultimap;
import com.thoughtworks.adtd.http.Response;

import java.util.List;

public class TestResponse implements Response {
    private final int status;
    private final LinkedListMultimap<String, String> headers;
    private final String body;

    public TestResponse(int status, String body) {
        this.status = status;
        this.body = body;
        headers = LinkedListMultimap.create();
    }

    public int getStatus() {
        return status;
    }

    public TestResponse addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public String getHeader(String name) {
        List<String> matches = headers.get(name);
        if (!matches.isEmpty()) {
            return matches.get(0);
        }
        return null;
    }

    public String getBody() throws Exception {
        return body;
    }
}
