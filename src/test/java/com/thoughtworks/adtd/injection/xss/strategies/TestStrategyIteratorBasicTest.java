package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.injection.xss.XssPayload;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestStrategyIteratorBasicTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldExhaustIterator() {
        TestStrategyIteratorBasic iterator = new TestStrategyIteratorBasic();
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
        TestStrategyIteratorBasic iterator = new TestStrategyIteratorBasic();
        while (iterator.hasNext()) {
            iterator.next();
        }
        expectedException.expect(NoSuchElementException.class);

        iterator.next();
    }

    @Test
    public void shouldThrowExceptionOnRemove() {
        TestStrategyIteratorBasic iterator = new TestStrategyIteratorBasic();
        expectedException.expect(UnsupportedOperationException.class);

        iterator.remove();
    }
}