package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestParameters;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestStrategyEmptyTokenTests {
    @Test
    public void shouldUpdateTokenToEmptyString() {
        int paramIndex = 0;
        Request requestMock = mock(Request.class);
        RequestParameters requestParamsMock = mock(RequestParameters.class);
        when(requestMock.getParams()).thenReturn(requestParamsMock);
        TestStrategyEmptyToken strategy = new TestStrategyEmptyToken(paramIndex);

        strategy.mutateRequest(requestMock);

        verify(requestParamsMock).setParam(paramIndex, "");
    }
}
