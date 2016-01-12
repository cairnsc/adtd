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
    private int testCount;

    /**
     * @param requestInfo RequestInfo containing headers to test.
     */
    public HttpResponseSplittingTestOrchestrator(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        currentIdx = 0;
        testCount = 0;
    }

    public boolean hasNext() {
        return (currentIdx < requestInfo.getRequestParameters().size());
    }

    public HttpResponseSplittingTest next() {
        HttpResponseSplittingTest test = new HttpResponseSplittingTest(requestInfo, currentIdx);
        ++currentIdx;
        ++testCount;
        return test;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get a count of the number of tests the orchestrator has run.
     * @return Number of tests the orchestrator has run.
     */
    public int countTested() {
        return testCount;
    }
}
