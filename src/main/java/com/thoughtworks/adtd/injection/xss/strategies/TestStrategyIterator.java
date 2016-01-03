package com.thoughtworks.adtd.injection.xss.strategies;

import java.util.Iterator;

/**
 * Interface for an XSS test strategy iterator. Responsible for iterating through test strategies for an XSS test
 * orchestrator.
 */
public interface TestStrategyIterator extends Iterator<TestStrategy> {
    int count();
}
