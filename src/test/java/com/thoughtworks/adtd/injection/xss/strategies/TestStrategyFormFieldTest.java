package com.thoughtworks.adtd.injection.xss.strategies;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.http.Request;
import com.thoughtworks.adtd.http.RequestExecutor;
import com.thoughtworks.adtd.injection.xss.XssPattern;
import org.junit.Test;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TestStrategyFormFieldTest {
    @Test
    public void shouldCreateRequestWithFormAndFormData() throws Exception {
        Form form = mock(Form.class);
        FormData formData = mock(FormData.class);
        String formField = "field";
        String testPattern = "test";
        XssPattern xssPattern = new XssPattern(testPattern);
        TestStrategyFormField strategy = new TestStrategyFormField(form, formData, formField, xssPattern);
        RequestExecutor requestExecutor = mock(RequestExecutor.class);
        Request request = mock(Request.class);
        when(form.createRequest(requestExecutor)).thenReturn(request);

        strategy.createRequest(requestExecutor);

        verify(form).createRequest(requestExecutor);
        InOrder inOrder = inOrder(formData);
        inOrder.verify(formData).setFormField(formField, testPattern);
        inOrder.verify(formData).setRequestParams(request);
        inOrder.verify(formData).setImmutable();
    }

    @Test
    public void shouldMatchWithXssPattern() {
        XssPattern xssPattern = mock(XssPattern.class);
        TestStrategyFormField strategy = new TestStrategyFormField(mock(Form.class), mock(FormData.class), "field", xssPattern);
        String content = "aaa";

        boolean matches = strategy.matches(content);

        verify(xssPattern).matches(content);
        assertThat(matches).isFalse();
    }
}