package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.http.ResponseValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CsrfTokenTestOrchestratorTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenTestOrchestrator iterator;
    private Form formMock;
    private ResponseValidator responseValidatorMock;

    @Before
    public void setUp() {
        formMock = mock(Form.class);
        responseValidatorMock = mock(ResponseValidator.class);
        iterator = new CsrfTokenTestOrchestrator(formMock, "test", responseValidatorMock);
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        expectedException.expect(UnsupportedOperationException.class);

        iterator.remove();
    }

    @Test
    public void shouldExhaustIterator() {
        int count = iterator.count();
        assertThat(count).isGreaterThan(0);

        int idx;
        for (idx = 0; idx < count; idx++) {
            CsrfTokenTest next = iterator.next();
            assertThat(next).isNotNull();
        }

        assertThat(idx).isEqualTo(count);
        assertThat(iterator.hasNext()).isFalse();
    }
}
