package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.injection.xss.XssPayload;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestStrategyBasicTest {
    @Test
    public void shouldMatchWithXssPattern() {
        XssPayload xssPayloadMock = mock(XssPayload.class);
        TestStrategyBasic strategy = new TestStrategyBasic(xssPayloadMock);
        String content = "aaa";

        boolean matches = strategy.matches(content);

        verify(xssPayloadMock).matches(content);
        assertThat(matches).isFalse();
    }
}