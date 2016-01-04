package com.thoughtworks.adtd.injection.http;

import com.thoughtworks.adtd.http.RequestInfo;

import java.util.Iterator;

/**
 * Orchestrator to test for HTTP response splitting.
 *
 * <p>Read about HTTP response splitting at <a href="https://www.owasp.org/index.php/HTTP_Response_Splitting">OWASP:
 * HTTP Response Splitting</a>.
 *
 * <p>The orchestrator sends a test request containing a dangerous payload for each HTTP header in a RequestInfo. The
 * response is evaluated for whether the payload resulted in a HTTP header being split.
 */
public class HttpResponseSplittingTestOrchestrator implements Iterator<HttpResponseSplittingTest> {
    private final RequestInfo requestInfo;
    private int currentIdx;

    /**
     * Instantiate a HttpResponseSplittingTestOrchestrator.
     * @param requestInfo RequestInfo containing headers to test.
     */
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
