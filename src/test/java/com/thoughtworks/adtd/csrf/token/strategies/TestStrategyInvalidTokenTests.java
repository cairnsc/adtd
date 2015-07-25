package com.thoughtworks.adtd.csrf.token.strategies;

import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.html.FormFieldData;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TestStrategyInvalidTokenTests {

    @Test
    public void shouldMutateToken() {
        String tokenInputName = "test";
        TestStrategyInvalidToken strategy = new TestStrategyInvalidToken(tokenInputName);
        FormData formData = mock(FormData.class);
        String tokenInputValue = "AAA-BBB-CCC";
        when(formData.getFormField(tokenInputName)).thenReturn(new FormFieldData(null, tokenInputName, tokenInputValue));

        strategy.mutateFormData(formData);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(formData).setFormField(eq(tokenInputName), argument.capture());
        assertThat(argument.getValue()).isNotEqualTo(tokenInputValue);
        assertThat(argument.getValue().length()).isEqualTo(tokenInputValue.length());
    }

}
