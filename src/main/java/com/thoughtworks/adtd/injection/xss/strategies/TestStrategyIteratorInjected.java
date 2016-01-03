package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.injection.xss.XssPayload;

import java.util.NoSuchElementException;

/**s
 * Test strategy iterator for tests where the XSS payload will be injected into the response. Intended primarily for use
 * when testing for persistent XSS.
 */
public class TestStrategyIteratorInjected implements TestStrategyIterator {
    private int currentIdx;

    /**
     * Instantiate a TestStrategyIteratorInjected.
     */
    public TestStrategyIteratorInjected() {
        currentIdx = 0;
    }

    public boolean hasNext() {
        return (currentIdx < XssPayload.PAYLOAD_LIST.length);
    }

    public TestStrategy next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        XssPayload payload = new XssPayload(XssPayload.PAYLOAD_LIST[currentIdx]);
        currentIdx++;
        return new TestStrategyInject(payload);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return XssPayload.PAYLOAD_LIST.length;
    }
}
