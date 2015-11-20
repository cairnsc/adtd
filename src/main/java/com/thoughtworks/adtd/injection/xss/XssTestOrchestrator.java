package com.thoughtworks.adtd.injection.xss;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.thoughtworks.adtd.injection.xss.strategies.TestStrategy;
import com.thoughtworks.adtd.injection.xss.strategies.TestStrategyIterator;

import java.util.Iterator;

public class XssTestOrchestrator implements Iterator<XssTest> {
    public static Iterable<Object[]> asIterableOfArrays(final TestStrategyIterator testStrategyIterator) {
        return FluentIterable
                .from(new Iterable<XssTest>() {
                    public Iterator<XssTest> iterator() {
                        return new XssTestOrchestrator(testStrategyIterator);
                    }
                })
                .transform(new Function<XssTest, Object[]>() {
                    public Object[] apply(XssTest input) {
                        return new Object[]{ input };
                    }
                });
    }

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
