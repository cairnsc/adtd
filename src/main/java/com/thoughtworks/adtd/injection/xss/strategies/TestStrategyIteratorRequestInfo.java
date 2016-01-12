package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.html.RequestParameterProperty;
import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestParameter;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.injection.xss.XssPayload;

import java.util.NoSuchElementException;

/**
 * Test strategy iterator to test XSS payloads against each request parameter in a RequestInfo. Intended to test for
 * reflected XSS.
 */
public class TestStrategyIteratorRequestInfo implements TestStrategyIterator {
    private final RequestInfo requestInfo;
    private int currentFormFieldIdx;
    private int nextFormFieldIdx;
    private int currentPayloadIdx;
    private int nextPayloadIdx;

    /**
     * @param requestInfo RequestInfo containing request parameters to test.
     */
    public TestStrategyIteratorRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        currentFormFieldIdx = nextFormFieldIdx = -1;
        getNextFormFieldIdx();
        currentPayloadIdx = nextPayloadIdx = -1;
    }

    public boolean hasNext() {
        if (currentPayloadIdx != nextPayloadIdx) {
            return true;
        }

        RequestParameters requestParameters = requestInfo.getRequestParameters();
        if (nextFormFieldIdx == requestParameters.size()) {
            return false;
        }

        nextPayloadIdx = currentPayloadIdx + 1;
        if (nextPayloadIdx == XssPayload.PAYLOAD_LIST.length) {
            getNextFormFieldIdx();
            if (nextFormFieldIdx == requestParameters.size()) {
                currentPayloadIdx = nextPayloadIdx;
                return false;
            }

            nextPayloadIdx = 0;
        }
        return true;
    }

    public TestStrategy next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        currentPayloadIdx = nextPayloadIdx;
        XssPayload payload = new XssPayload(XssPayload.PAYLOAD_LIST[currentPayloadIdx]);
        currentFormFieldIdx = nextFormFieldIdx;
        return new TestStrategyRequestParam(requestInfo, currentFormFieldIdx, payload);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void getNextFormFieldIdx() {
        RequestParameters requestParameters = requestInfo.getRequestParameters();
        ++nextFormFieldIdx;
        while (nextFormFieldIdx < requestParameters.size()) {
            RequestParameter param = requestParameters.getParam(nextFormFieldIdx);
            if (!param.getProperties().contains(RequestParameterProperty.REQUEST_PARAMETER_IGNORE)) {
                break;
            }

            ++nextFormFieldIdx;
        }
    }
}
