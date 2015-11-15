package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.Request;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
    public void shouldPopulateFormDataImplWithFormInputs() {
        Form formMock = mock(Form.class);
        ArrayList<FormFieldData> fieldsInForm = newArrayList();
        fieldsInForm.add(new FormFieldData(null, "a", "b"));
        fieldsInForm.add(new FormFieldData(null, "c", "d"));
        when(formMock.getFormFields()).thenReturn(fieldsInForm);

        FormData formData = new FormData(formMock);

        List<FormFieldData> formDataFields = formData.getFormFields();
        assertThat(formDataFields.size()).isEqualTo(2);
        assertThat(formDataFields.size()).isEqualTo(formData.countFormFields());
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
        assertThat(formDataFields.size()).isEqualTo(formData.countFormFields());
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
        assertThat(formFieldData).isSameAs(formData.getFormField(1));
    }

    @Test
    public void shouldReturnNullIfFormFieldNotFound() {
        FormData formData = new FormData();

        formData.addFormField("test", "test");

        FormFieldData formFieldData = formData.getFormField("tset");
        assertThat(formFieldData).isNull();
    }

    @Test
    public void shouldSetFormFieldByName() {
        FormData formData = new FormData();
        String name = "a";
        formData.addFormField(name, "b");

        String newValue = "new value";
        formData.setFormField(name, newValue);

        FormFieldData formFieldData = formData.getFormField(name);
        assertThat(formFieldData.getValue()).isEqualTo(newValue);
    }

    @Test
    public void shouldSetFormFieldByIndex() {
        FormData formData = new FormData();
        String name = "a";
        formData.addFormField(name, "b");

        String newValue = "new value";
        formData.setFormField(0, newValue);

        FormFieldData formFieldData = formData.getFormField(name);
        assertThat(formFieldData.getValue()).isEqualTo(newValue);
    }

    @Test
    public void shouldThrowExceptionWhenImmutableInSetFormFieldByName() {
        FormData formData = new FormData();
        formData.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formData.setFormField("a", "new value");
    }

    @Test
    public void shouldThrowExceptionWhenImmutableInSetFormFieldByIndex() {
        FormData formData = new FormData();
        formData.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formData.setFormField(0, "new value");
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
        assertThat(formDataFields.size()).isEqualTo(formData.countFormFields());
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

    @Test
    public void shouldSetRequestParams() {
        FormData formData = new FormData();
        formData.setFormField("a", "b");
        formData.setFormField("c", "d");
        Request requestMock = mock(Request.class);

        formData.setRequestParams(requestMock);

        verify(requestMock).param("a", "b");
        verify(requestMock).param("c", "d");
    }
}
