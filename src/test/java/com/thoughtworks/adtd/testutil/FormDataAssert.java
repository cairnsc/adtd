package com.thoughtworks.adtd.testutil;

import com.thoughtworks.adtd.html.Form;
import com.thoughtworks.adtd.html.FormData;
import com.thoughtworks.adtd.html.FormFieldData;
import org.assertj.core.api.AbstractAssert;

import java.util.List;

import static com.thoughtworks.adtd.testutil.Assertions.assertThat;

public class FormDataAssert extends AbstractAssert<FormDataAssert, FormData> {
    private FormToFormDataFieldComparator formToFormDataFieldComparator = new FormToFormDataFieldComparator();
    private FormData actual;

    protected FormDataAssert(FormData actual) {
        super(actual, FormDataAssert.class);
        this.actual = actual;
    }

    public FormDataAssert isDerivedFrom(Form form) {
        isNotNull();
        List<FormFieldData> formFields = form.getFormFields();
        List<FormFieldData> formDataFields = actual.getFormFields();
        assertThat(formDataFields).usingElementComparator(formToFormDataFieldComparator)
                .containsExactlyElementsOf(formFields);
        return this;
    }
}
