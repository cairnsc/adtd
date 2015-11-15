package com.thoughtworks.adtd.injection.xss;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class XssTestOrchestratorTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private XssTestOrchestrator orchestrator;

    @Before
    public void setUp() {
        orchestrator = new XssTestOrchestrator();
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        expectedException.expect(UnsupportedOperationException.class);

        orchestrator.remove();
    }

    @Test
    public void shouldExhaustIterator() {
        int count = orchestrator.count();
        assertThat(count).isGreaterThan(0);

        int idx;
        for (idx = 0; idx < count; idx++) {
            XssTest next = orchestrator.next();
            assertThat(next).isNotNull();
        }

        assertThat(idx).isEqualTo(count);
        assertThat(orchestrator.hasNext()).isFalse();
    }
}
