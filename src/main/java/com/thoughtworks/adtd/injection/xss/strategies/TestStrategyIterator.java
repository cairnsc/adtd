package com.thoughtworks.adtd.injection.xss.strategies;

import java.util.Iterator;

/**
 * Interface for an XSS test strategy iterator. Responsible for iterating through test strategies for an XSS test
 * orchestrator.
 */
public interface TestStrategyIterator extends Iterator<TestStrategy> {
    /**
     * Get a count of the number of test strategies to iterate through.
     * @return The number of test strategies.
     */
    int count();
}
