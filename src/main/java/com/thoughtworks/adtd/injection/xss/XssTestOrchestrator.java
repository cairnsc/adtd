package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.injection.xss.strategies.TestStrategy;
import com.thoughtworks.adtd.injection.xss.strategies.TestStrategyIterator;

import java.util.Iterator;

/**
 * Orchestrator to verify Cross-site Scripting (XSS) protection. The tests perform basic pattern matching against a
 * response.
 *
 * <p>Read about XSS at <a href="https://www.owasp.org/index.php/Cross-site_Scripting_(XSS)">OWASP: Cross-site Scripting
 * (XSS)</a>. Further reading about how to evade XSS filters can be found at <a href="https://www.owasp.org/index.php/XSS_Filter_Evasion_Cheat_Sheet">
 * OWASP: XSS Filter Evasion Cheat Sheet</a>.
 *
 * <p>The orchestrator sends test requests containing potentially dangerous payloads to verify XSS protection is
 * functioning correctly. Requests are provided by a {@link com.thoughtworks.adtd.injection.xss.strategies.TestStrategyIterator}.
 * The response is evaluated by matching the payloads against the response body.
 */
public class XssTestOrchestrator implements Iterator<XssTest> {
    private TestStrategyIterator testStrategyIterator;
    private int testCount;

    public XssTestOrchestrator(TestStrategyIterator testStrategyIterator) {
        this.testStrategyIterator = testStrategyIterator;
        testCount = 0;
    }

    public boolean hasNext() {
        return testStrategyIterator.hasNext();
    }

    public XssTest next() {
        TestStrategy testStrategy = testStrategyIterator.next();
        XssTest test = new XssTest(testStrategy);
        ++testCount;
        return test;
    }

    public void remove() {
        testStrategyIterator.remove();
    }

    /**
     * Get a count of the number of tests the orchestrator has run.
     * @return Number of tests the orchestrator has run.
     */
    public int countTested() {
        return testCount;
    }
}
