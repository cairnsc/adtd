package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.injection.xss.XssPayload;

import java.util.NoSuchElementException;

public class TestStrategyIteratorBasic implements TestStrategyIterator {
    private int currentIdx;

    public TestStrategyIteratorBasic() {
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
        return new TestStrategyBasic(payload);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int count() {
        return XssPayload.PAYLOAD_LIST.length;
    }
}
