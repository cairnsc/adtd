package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.injection.xss.XssPattern;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestStrategyBasicTest {
    @Test
    public void shouldMatchWithXssPattern() {
        XssPattern xssPattern = mock(XssPattern.class);
        TestStrategyBasic strategy = new TestStrategyBasic(xssPattern);
        String content = "aaa";

        boolean matches = strategy.matches(content);

        verify(xssPattern).matches(content);
        assertThat(matches).isFalse();
    }
}