package com.thoughtworks.adtd.injection.http;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.injection.xss.XssTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpResponseSplittingTestOrchestratorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private Form form;
    public HttpResponseSplittingTestOrchestrator orchestrator;

    @Before
    public void setUp() {
        form = mock(Form.class);
        orchestrator = new HttpResponseSplittingTestOrchestrator(form);
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        expectedException.expect(UnsupportedOperationException.class);

        orchestrator.remove();
    }

    @Test
    public void shouldGetCountFromForm() {
        int formFieldCount = 123;
        when(form.countFormFields()).thenReturn(formFieldCount);

        int count = orchestrator.count();

        assertThat(count).isEqualTo(formFieldCount);
    }

    @Test
    public void shouldExhaustIterator() {
        int formFieldCount = 1;
        when(form.countFormFields()).thenReturn(formFieldCount);
        int count = orchestrator.count();
        assertThat(count).isEqualTo(formFieldCount);

        int idx;
        for (idx = 0; idx < count; idx++) {
            HttpResponseSplittingTest next = orchestrator.next();
            assertThat(next).isNotNull();
        }

        assertThat(idx).isEqualTo(count);
        assertThat(orchestrator.hasNext()).isFalse();
    }
}