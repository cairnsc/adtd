package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.html.FormData;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TestStrategyEmptyTokenTests {

    @Test
    public void shouldUpdateTokenToEmptyString() {
        String tokenInputName = "test";
        TestStrategyEmptyToken strategy = new TestStrategyEmptyToken(tokenInputName);
        FormData formData = mock(FormData.class);

        strategy.mutateFormData(formData);

        verify(formData).setFormField(tokenInputName, "");
    }

}
