package com.thoughtworks.adtd.injection.http;

import com.thoughtworks.adtd.http.RequestInfo;

import java.util.Iterator;

/**
 * Orchestrator for HTTP response splitting tests.
 */
public class HttpResponseSplittingTestOrchestrator implements Iterator<HttpResponseSplittingTest> {
    private final RequestInfo requestInfo;
    private int currentIdx;

    public HttpResponseSplittingTestOrchestrator(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        currentIdx = 0;
    }

    public boolean hasNext() {
        return (currentIdx < requestInfo.getRequestParameters().size());
    }

    public HttpResponseSplittingTest next() {
        HttpResponseSplittingTest test = new HttpResponseSplittingTest(requestInfo, currentIdx);
        currentIdx++;
        return test;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return requestInfo.getRequestParameters().size();
    }
}
