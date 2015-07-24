package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.Request;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FormDataImplTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBeMutable() {
        FormDataImpl formData = new FormDataImpl();

        assertThat(formData.isMutable()).isTrue();
    }

    @Test
    public void shouldPopulateFormDataImplWithFormInputs() {
        Form form = mock(Form.class);
        ArrayList<FormFieldData> fieldsInForm = new ArrayList<FormFieldData>();
        fieldsInForm.add(new FormFieldData(null, "a", "b"));
        fieldsInForm.add(new FormFieldData(null, "c", "d"));
        when(form.getFormFields()).thenReturn(fieldsInForm);

        FormDataImpl formData = new FormDataImpl(form);

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
        FormDataImpl formData = new FormDataImpl();

        formData.setImmutable();

        assertThat(formData.isMutable()).isFalse();
    }

    @Test
    public void shouldAddFormField() {
        FormDataImpl formData = new FormDataImpl();

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
        FormDataImpl formData = new FormDataImpl();
        formData.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formData.addFormField("a", "b");
    }

    @Test
    public void shouldGetFormField() {
        FormDataImpl formData = new FormDataImpl();

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
        FormDataImpl formData = new FormDataImpl();

        formData.addFormField("test", "test");

        FormFieldData formFieldData = formData.getFormField("tset");
        assertThat(formFieldData).isNull();
    }

    @Test
    public void shouldSetFormField() {
        FormDataImpl formData = new FormDataImpl();
        String name = "a";
        formData.addFormField(name, "b");

        String value = "new value";
        formData.setFormField(name, value);

        FormFieldData formFieldData = formData.getFormField(name);
        assertThat(formFieldData.getValue()).isEqualTo(value);
    }

    @Test
    public void shouldThrowExceptionWhenImmutableInSetFormField() {
        FormDataImpl formData = new FormDataImpl();
        formData.setFormField("a", "b");
        formData.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formData.setFormField("a", "new value");
    }

    @Test
    public void shouldAddFormFieldInSetIfFieldDoesNotExist() {
        FormDataImpl formData = new FormDataImpl();
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
        FormDataImpl formData = new FormDataImpl();
        formData.setImmutable();
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Form is immutable");

        formData.setFormField("a", "b");
    }

    @Test
    public void shouldSetRequestParams() {
        FormDataImpl formData = new FormDataImpl();
        formData.setFormField("a", "b");
        formData.setFormField("c", "d");
        Request request = mock(Request.class);

        formData.setRequestParams(request);

        verify(request).param("a", "b");
        verify(request).param("c", "d");
    }

}
