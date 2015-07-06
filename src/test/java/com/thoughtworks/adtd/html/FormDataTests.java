package com.thoughtworks.adtd.html;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FormDataTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBeMutable() {
        FormData formData = new FormData();

        assertThat(formData.isMutable()).isTrue();
    }

    @Test
    public void shouldPopulateFormDataWithFormInputs() {
        Form form = mock(Form.class);
        ArrayList<FormFieldData> fieldsInForm = new ArrayList<FormFieldData>();
        fieldsInForm.add(new FormFieldData(null, "a", "b"));
        fieldsInForm.add(new FormFieldData(null, "c", "d"));
        when(form.getFormFields()).thenReturn(fieldsInForm);

        FormData formData = new FormData(form);

        List<FormFieldData> formDataFields = formData.getFormFields();
        assertThat(formDataFields.size()).isEqualTo(2);
        FormFieldData formFieldData = formDataFields.get(0);
        assertThat(formFieldData.getName()).isEqualTo("a");
        assertThat(formFieldData.getValue()).isEqualTo("b");
        formFieldData = formDataFields.get(1);
        assertThat(formFieldData.getName()).isEqualTo("c");
        assertThat(formFieldData.getValue()).isEqualTo("d");
    }

    @Test
    public void shouldSetImmutable() {
        FormData formData = new FormData();

        formData.setImmutable();

        assertThat(formData.isMutable()).isFalse();
    }

    @Test
    public void shouldAddFormField() {
        FormData formData = new FormData();

        String name = "a";
        String value = "b";
        formData.addFormField(name, value);

        List<FormFieldData> formDataFields = formData.getFormFields();
        assertThat(formDataFields.size()).isEqualTo(1);
        FormFieldData formFieldData = formDataFields.get(0);
        assertThat(formFieldData.getName()).isEqualTo(name);
        assertThat(formFieldData.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldThrowExceptionWhenImmutableInAddFormField() {
        FormData formData = new FormData();
        formData.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formData.addFormField("a", "b");
    }

    @Test
    public void shouldGetFormField() {
        FormData formData = new FormData();

        String name = "a";
        String value = "b";
        formData.addFormField("c", "d");
        formData.addFormField(name, value);
        formData.addFormField("e", "f");

        FormFieldData formFieldData = formData.getFormField(name);
        assertThat(formFieldData.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldReturnNullIfFormFieldNotFound() {
        FormData formData = new FormData();

        formData.addFormField("test", "test");

        FormFieldData formFieldData = formData.getFormField("tset");
        assertThat(formFieldData).isNull();
    }

    @Test
    public void shouldSetFormField() {
        FormData formData = new FormData();
        String name = "a";
        formData.addFormField(name, "b");

        String value = "new value";
        formData.setFormField(name, value);

        FormFieldData formFieldData = formData.getFormField(name);
        assertThat(formFieldData.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldThrowExceptionWhenImmutableInSetFormField() {
        FormData formData = new FormData();
        formData.setFormField("a", "b");
        formData.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formData.setFormField("a", "new value");
    }

    @Test
    public void shouldAddFormFieldInSetIfFieldDoesNotExist() {
        FormData formData = new FormData();
        formData.addFormField("a", "b");

        String name = "c";
        String value = "d";
        formData.setFormField(name, value);

        List<FormFieldData> formDataFields = formData.getFormFields();
        assertThat(formDataFields.size()).isEqualTo(2);
        FormFieldData formFieldData = formData.getFormField(name);
        assertThat(formFieldData.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldThrowExceptionWhenImmutableInSetFormFieldIfFieldDoesNotExist() {
        FormData formData = new FormData();
        formData.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formData.setFormField("a", "b");
    }

}
