package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.injection.xss.XssPayload;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestStrategyIteratorInjectedTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldExhaustIterator() {
        TestStrategyIteratorInjected iterator = new TestStrategyIteratorInjected();
        int count = iterator.count();
        assertThat(count).isEqualTo(XssPayload.PAYLOAD_LIST.length);

        int idx;
        for (idx = 0; idx < count; idx++) {
            TestStrategy next = iterator.next();
            assertThat(next).isNotNull();
        }

        assertThat(idx).isEqualTo(count);
        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void shouldThrowExceptionOnNextWhenIteratorExhausted() {
        TestStrategyIteratorInjected iterator = new TestStrategyIteratorInjected();
        while (iterator.hasNext()) {
            iterator.next();
        }
        expectedException.expect(NoSuchElementException.class);

        iterator.next();
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        TestStrategyIteratorInjected iterator = new TestStrategyIteratorInjected();
        expectedException.expect(UnsupportedOperationException.class);

        iterator.remove();
    }
}