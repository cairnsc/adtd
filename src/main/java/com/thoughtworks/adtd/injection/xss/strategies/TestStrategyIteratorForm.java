package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.injection.xss.XssPayload;

import java.util.NoSuchElementException;

/**
 * Test strategy iterator to iterate over the fields in a form.
 */
public class TestStrategyIteratorForm implements TestStrategyIterator {
    private final RequestInfo requestInfo;
    private int currentFormFieldIdx;
    private int currentPayloadIdx;

    public TestStrategyIteratorForm(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        currentFormFieldIdx = 0;
        currentPayloadIdx = 0;
    }

    public boolean hasNext() {
        return (currentFormFieldIdx < requestInfo.getRequestParameters().size());
    }

    public TestStrategy next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        XssPayload payload = new XssPayload(XssPayload.PAYLOAD_LIST[currentPayloadIdx]);
        currentPayloadIdx++;
        if (currentPayloadIdx == XssPayload.PAYLOAD_LIST.length) {
            currentFormFieldIdx++;
            currentPayloadIdx = 0;
        }
        return new TestStrategyFormField(requestInfo, currentFormFieldIdx, payload);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return requestInfo.getRequestParameters().size() * XssPayload.PAYLOAD_LIST.length;
    }
}
