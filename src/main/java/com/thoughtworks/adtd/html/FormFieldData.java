package com.thoughtworks.adtd.html;

public class FormFieldData {

    private final FormData formData;
    private String name;
    private String value;

    public FormFieldData(FormData formData, String name, String value) {
        this.formData = formData;
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        checkMutability();
        this.value = value;
    }

    private void checkMutability() {
        if (formData == null || !formData.isMutable()) {
            throw new IllegalStateException("Form is immutable");
        }
    }

}
