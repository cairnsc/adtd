package com.thoughtworks.adtd.injection.xss;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class XssTestIteratorImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private XssTestIteratorImpl iterator;

    @Before
    public void setUp() {
        iterator = new XssTestIteratorImpl();
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
            XssTest next = iterator.next();
            assertThat(next).isNotNull();
        }

        assertThat(iterator.hasNext()).isFalse();
    }

}
