package com.thoughtworks.adtd.injection.xss;

import com.thoughtworks.adtd.injection.xss.strategies.TestStrategy;
import com.thoughtworks.adtd.injection.xss.strategies.TestStrategyIterator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class XssTestOrchestratorTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private TestStrategyIterator testStrategyIterator;
    private XssTestOrchestrator orchestrator;

    @Before
    public void setUp() {
        testStrategyIterator = mock(TestStrategyIterator.class);
        orchestrator = new XssTestOrchestrator(testStrategyIterator);
    }

    @Test
    public void shouldHaveZeroTestedCount() {
        assertThat(orchestrator.countTested()).isZero();
    }

    @Test
    public void shouldUseIteratorInHasNext() {
        when(testStrategyIterator.hasNext()).thenReturn(true);

        boolean hasNext = orchestrator.hasNext();

        verify(testStrategyIterator).hasNext();
        assertThat(hasNext).isTrue();
        assertThat(orchestrator.countTested()).isZero();
    }

    @Test
    public void shouldGetNext() {
        TestStrategy testStrategy = mock(TestStrategy.class);
        XssPayload xssPayload = new XssPayload("abc123");
        when(testStrategy.getXssPayload()).thenReturn(xssPayload);
        when(testStrategyIterator.next()).thenReturn(testStrategy);

        XssTest next = orchestrator.next();

        assertThat(next.getXssPayload()).isEqualTo(xssPayload);
        assertThat(orchestrator.countTested()).isEqualTo(1);
    }

    @Test
    public void shouldUseIteratorRemove() {
        orchestrator.remove();

        verify(testStrategyIterator).remove();
    }
}
