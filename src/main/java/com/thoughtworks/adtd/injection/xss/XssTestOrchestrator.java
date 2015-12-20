package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.injection.xss.strategies.TestStrategy;
import com.thoughtworks.adtd.injection.xss.strategies.TestStrategyIterator;

import java.util.Iterator;

public class XssTestOrchestrator implements Iterator<XssTest> {
    private TestStrategyIterator testStrategyIterator;

    public XssTestOrchestrator(TestStrategyIterator testStrategyIterator) {
        this.testStrategyIterator = testStrategyIterator;
    }

    public boolean hasNext() {
        return testStrategyIterator.hasNext();
    }

    public XssTest next() {
        TestStrategy testStrategy = testStrategyIterator.next();
        XssTest test = new XssTest(testStrategy);
        return test;
    }

    public void remove() {
        testStrategyIterator.remove();
    }

    public int count() {
        return testStrategyIterator.count();
    }
}
