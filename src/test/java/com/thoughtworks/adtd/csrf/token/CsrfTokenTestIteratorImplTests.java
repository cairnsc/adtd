package com.thoughtworks.adtd.csrf.token;

import com.thoughtworks.adtd.http.ResponseValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CsrfTokenTestIteratorImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CsrfTokenTestIteratorImpl iterator;

    @Before
    public void setUp() {
        iterator = new CsrfTokenTestIteratorImpl("test", "test", mock(ResponseValidator.class));
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

        for (int idx = 0; idx < count; idx++) {
            CsrfTokenTest next = iterator.next();
            assertThat(next).isNotNull();
        }

        assertThat(iterator.hasNext()).isFalse();
    }

}
