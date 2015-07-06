package com.thoughtworks.adtd.html;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormFieldDataTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowExceptionInSetValueIfFormDataNotSet() {
        FormFieldData formFieldData = new FormFieldData(null, "test", "test");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formFieldData.setValue("tset");
    }

    @Test
    public void shouldThrowExceptionInSetValueIfFormDataIsImmutable() {
        FormData formData = mock(FormData.class);
        when(formData.isMutable()).thenReturn(false);
        FormFieldData formFieldData = new FormFieldData(formData, "test", "test");
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formFieldData.setValue("tset");
    }

    @Test
    public void shouldSetValue() {
        FormData formData = mock(FormData.class);
        when(formData.isMutable()).thenReturn(true);
        FormFieldData formFieldData = new FormFieldData(formData, "test", "test");

        String value = "tset";
        formFieldData.setValue(value);

        assertThat(formFieldData.getValue()).isEqualTo(value);
    }

}
