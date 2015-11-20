package com.thoughtworks.adtd.injection.xss.strategies;

import java.util.Iterator;

public interface TestStrategyIterator extends Iterator<TestStrategy> {
    int count();
}
