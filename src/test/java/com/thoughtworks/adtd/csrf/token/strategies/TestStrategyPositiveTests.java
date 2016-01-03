package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class TestStrategyPositiveTests {
    @Test
    public void shouldReturnTestStrategyName() {
        TestStrategyPositive strategy = new TestStrategyPositive(0);

        String strategyName = strategy.getStrategyName();

        assertThat(strategyName).isEqualTo(TestStrategyPositive.STRATEGY_NAME);
    }

    @Test
    public void shouldNotMutateFormData() {
        TestStrategyPositive strategy = new TestStrategyPositive(0);
        Request requestMock = mock(Request.class);

        strategy.mutateRequest(requestMock);

        verifyZeroInteractions(requestMock);
    }


}
