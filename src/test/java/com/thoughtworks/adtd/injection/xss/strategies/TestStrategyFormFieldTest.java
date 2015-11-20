package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.injection.xss.XssPayload;
import org.junit.Test;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TestStrategyFormFieldTest {
    @Test
    public void shouldCreateRequestWithFormAndFormData() throws Exception {
        Form formMock = mock(Form.class);
        FormData formDataMock = mock(FormData.class);
        int formFieldIdx = 2;
        String testPattern = "test";
        XssPayload xssPayload = new XssPayload(testPattern);
        TestStrategyFormField strategy = new TestStrategyFormField(formMock, formDataMock, formFieldIdx, xssPayload);
        RequestExecutor requestExecutor = mock(RequestExecutor.class);
        Request request = mock(Request.class);
        when(formMock.createRequest(requestExecutor)).thenReturn(request);

        strategy.createRequest(requestExecutor);

        verify(formMock).createRequest(requestExecutor);
        InOrder inOrder = inOrder(formDataMock);
        inOrder.verify(formDataMock).setFormField(formFieldIdx, testPattern);
        inOrder.verify(formDataMock).setRequestParams(request);
        inOrder.verify(formDataMock).setImmutable();
    }

    @Test
    public void shouldMatchWithXssPattern() {
        XssPayload xssPayloadMock = mock(XssPayload.class);
        TestStrategyFormField strategy = new TestStrategyFormField(mock(Form.class), mock(FormData.class), 1, xssPayloadMock);
        String content = "aaa";

        boolean matches = strategy.matches(content);

        verify(xssPayloadMock).matches(content);
        assertThat(matches).isFalse();
    }
}