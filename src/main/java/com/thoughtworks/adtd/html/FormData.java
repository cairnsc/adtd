package com.thoughtworks.adtd.html;

import com.thoughtworks.adtd.http.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Data to submit in a form. Permits fields with duplicate names. Attempts to retain order of fields.
 */
public class FormData {
    private final List<FormFieldData> formFields;
    private boolean isMutable;

    public FormData() {
        formFields = new ArrayList<FormFieldData>();
        isMutable = true;
    }

    public FormData(Form form) {
        this();
        for (FormFieldData formFieldData : form.getFormFields()) {
            formFields.add(new FormFieldData(this, formFieldData.getName(), formFieldData.getValue()));
        }
    }

    public List<FormFieldData> getFormFields() {
        return formFields;
    }

    public boolean isMutable() {
        return isMutable;
    }

    public void setImmutable() {
        isMutable = false;
    }

    public FormFieldData getFormField(String name) {
        for (FormFieldData formFieldData : formFields) {
            if (formFieldData.getName().equals(name)) {
                return formFieldData;
            }
        }
        return null;
    }

    public void setFormField(String name, String value) {
        checkMutability();
        FormFieldData formField = getFormField(name);
        if (formField != null) {
            formField.setValue(value);
        } else {
            formFields.add(new FormFieldData(this, name, value));
        }
    }

    public void addFormField(String name, String value) {
        checkMutability();
        formFields.add(new FormFieldData(this, name, value));
    }

    public void setRequestParams(Request request) {
        for (FormFieldData formField : formFields) {
            request.param(formField.getName(), formField.getValue());
        }
    }

    private void checkMutability() {
        if (!isMutable) {
            throw new IllegalStateException("Form is immutable");
        }
    }
}
