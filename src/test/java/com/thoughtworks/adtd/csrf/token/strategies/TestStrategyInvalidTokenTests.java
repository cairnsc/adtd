package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestParameterImpl;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.util.AssertionFailureException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TestStrategyInvalidTokenTests {
    @Test
    public void shouldReturnTestStrategyName() {
        TestStrategyInvalidToken strategy = new TestStrategyInvalidToken(0);

        String strategyName = strategy.getStrategyName();

        assertThat(strategyName).isEqualTo(TestStrategyInvalidToken.STRATEGY_NAME);
    }

    @Test
    public void shouldMutateToken() throws AssertionFailureException {
        int paramIndex = 1;
        String tokenInputValue = "AAA-BBB-CCC";
        Request requestMock = mock(Request.class);
        RequestParameters requestParamsMock = mock(RequestParameters.class);
        when(requestMock.getParams()).thenReturn(requestParamsMock);
        RequestParameterImpl requestParameter = new RequestParameterImpl("test", tokenInputValue);
        when(requestParamsMock.getParam(paramIndex)).thenReturn(requestParameter);
        TestStrategyInvalidToken strategy = new TestStrategyInvalidToken(paramIndex);

        strategy.mutateRequest(requestMock);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(requestParamsMock).setParam(eq(paramIndex), argument.capture());
        assertThat(argument.getValue()).isNotEqualTo(tokenInputValue);
        assertThat(argument.getValue().length()).isEqualTo(tokenInputValue.length());
    }
}
