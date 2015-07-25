package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.html.FormData;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class TestStrategyPositiveTests {

    @Test
    public void shouldNotMutateFormData() {
        TestStrategyPositive strategy = new TestStrategyPositive();
        FormData formData = mock(FormData.class);

        strategy.mutateFormData(formData);

        verifyZeroInteractions(formData);
    }

}
