package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestInfo;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.http.RequestParameters;
import com.thoughtworks.adtd.injection.xss.XssPayload;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TestStrategyRequestParamTests {
    @Test
    public void shouldCreateRequestWithFormAndFormData() throws Exception {
        RequestInfo requestInfoMock = mock(RequestInfo.class);
        int paramIndex = 2;
        String testPattern = "test";
        XssPayload xssPayload = new XssPayload(testPattern);
        TestStrategyRequestParam strategy = new TestStrategyRequestParam(requestInfoMock, paramIndex, xssPayload);
        RequestExecutor requestExecutor = mock(RequestExecutor.class);
        Request requestMock = mock(Request.class);
        when(requestInfoMock.createRequest(requestExecutor, null)).thenReturn(requestMock);
        RequestParameters requestParametersMock = mock(RequestParameters.class);
        when(requestMock.getParams()).thenReturn(requestParametersMock);

        strategy.createRequest(requestExecutor);

        verify(requestInfoMock).createRequest(requestExecutor, null);
        verify(requestParametersMock).setParam(paramIndex, testPattern);
    }

    @Test
    public void shouldMatchWithXssPattern() {
        XssPayload xssPayloadMock = mock(XssPayload.class);
        TestStrategyRequestParam strategy = new TestStrategyRequestParam(mock(RequestInfo.class), 1, xssPayloadMock);
        String content = "aaa";

        boolean matches = strategy.matches(content);

        verify(xssPayloadMock).matches(content);
        assertThat(matches).isFalse();
    }
}